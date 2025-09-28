package com.example.lazyco.backend.core.BatchJob.SpringBatch;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.BatchJob.BatchJob;
import com.example.lazyco.backend.core.BatchJob.BatchJobDTO;
import com.example.lazyco.backend.core.BatchJob.BatchJobService;
import com.example.lazyco.backend.core.DateUtils.DateTimeZoneUtils;
import com.example.lazyco.backend.core.File.FileDTO;
import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Getter;
import lombok.Setter;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.RecordFieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Component
public abstract class AbstractSpringBatchJob<T, P extends AbstractDTO<?>>
    implements JobExecutionListener {

  private JobRepository jobRepository;
  private JobLauncher jobLauncher;
  private PlatformTransactionManager transactionManager;
  private BatchJobService batchJobService;
  private JobExplorer jobExplorer;
  private JobOperator jobOperator;

  @Autowired
  public void injectDependencies(
      JobRepository jobRepository,
      JobLauncher jobLauncher,
      PlatformTransactionManager transactionManager,
      BatchJobService batchJobService,
      JobExplorer jobExplorer,
      JobOperator jobOperator) {

    this.jobRepository = jobRepository;
    this.jobLauncher = jobLauncher;
    this.transactionManager = transactionManager;
    this.batchJobService = batchJobService;
    this.jobExplorer = jobExplorer;
    this.jobOperator = jobOperator;
  }

  /** -- GETTER -- Get the current batch job DTO */
  @Setter @Getter protected BatchJobDTO batchJobDTO;

  protected String jobName;
  protected int chunkSize = 1; // Default chunk size - will be overridden based on session type

  // CSV audit tracking
  protected int currentChunkNumber = 0;

  // Sequential counter for item tracking
  protected AtomicInteger itemCounter = new AtomicInteger(0);

  /**
   * Constructor for Spring Batch job (uses default chunk size of 20)
   *
   * @param jobName Name of the batch job
   */
  public AbstractSpringBatchJob(String jobName) {
    this.jobName = jobName;
  }

  /**
   * Constructor for Spring Batch job with custom chunk size
   *
   * @param jobName Name of the batch job
   * @param chunkSize Number of items to process in each chunk
   */
  public AbstractSpringBatchJob(String jobName, int chunkSize) {
    this.jobName = jobName;
    this.chunkSize = chunkSize;
  }

  /** Launch a Spring Batch job in the background */
  public void executeJob(List<T> inputData, String jobName) {
    try {
      ApplicationLogger.info(
          "Executing Spring Batch job: " + jobName + " with " + inputData.size() + " items");
      this.executeJobInBackground(inputData, jobName);
    } catch (Exception e) {
      ApplicationLogger.error("Failed to execute Spring Batch job: " + jobName, e);
      throw new RuntimeException("Batch job execution failed", e);
    }
  }

  /**
   * Create and execute a Spring Batch job
   *
   * @param inputData List of items to process
   * @param batchJobName Display name for the job
   */
  public void executeJobInBackground(List<T> inputData, String batchJobName) {
    try {
      this.batchJobDTO = new BatchJobDTO(); // Direct assignment, removing redundant variable

      // Create batch job record
      createBatchJobRecord(batchJobName, inputData.size());

      // Configure chunk size based on session type
      configureChunkSizeBasedOnSessionType(inputData);

      Job job = createJob(inputData);
      JobParameters jobParameters =
          new JobParametersBuilder()
              .addLong("timestamp", System.currentTimeMillis())
              .addLong("batchJobId", batchJobDTO.getId())
              .toJobParameters();
      ApplicationLogger.info(
          "Starting Spring Batch job: " + jobName + " with " + inputData.size() + " items");
      jobLauncher.run(job, jobParameters);
    } catch (Exception e) {
      ApplicationLogger.error("[BATCH] Exception in executeJob: " + e.getMessage(), e);
      if (batchJobDTO != null) {
        updateJobStatus(BatchJob.BatchJobStatus.FAILED);
      }
    }
  }

  /**
   * Configure chunk size based on session type: - ATOMIC_OPERATION: chunk size = input size
   * (all-or-nothing) - SINGLE_OBJECT_COMMIT: chunk size = 1 (individual commits) -
   * BATCH_WIDE_COMMIT or null: use default single commit behavior (chunk size = 1)
   */
  protected void configureChunkSizeBasedOnSessionType(List<T> inputData) {
    if (batchJobDTO == null || batchJobDTO.getSessionType() == null) {
      // Default behavior - single commit with chunk size = 1
      this.chunkSize = 1;

      System.out.println("Using default chunk size: " + chunkSize + " (single commit behavior)");
      return;
    }

    BatchJob.BatchJobSessionType sessionType = batchJobDTO.getSessionType();

    switch (sessionType) {
      case ATOMIC_OPERATION:
        // All-or-nothing: process entire input as one chunk
        if (inputData != null && !inputData.isEmpty()) {
          this.chunkSize = inputData.size();
          System.out.println(
              "ATOMIC_OPERATION: Setting chunk size to " + chunkSize + " (all-or-nothing)");
        }
        break;

      case SINGLE_OBJECT_COMMIT:
      default:
        // Individual commits: process one item at a time
        this.chunkSize = 1;
        System.out.println(
            "SINGLE_OBJECT_COMMIT: Setting chunk size to " + chunkSize + " (individual commits)");
        break;
    }
  }

  /**
   * Create the Spring Batch Job configuration
   *
   * @param inputData Input data for processing
   * @return Configured Spring Batch Job
   */
  protected Job createJob(List<T> inputData) {
    try {
      return new JobBuilder(jobName, jobRepository)
          .listener(this)
          .start(createProcessingStep(inputData))
          .build();
    } catch (Exception e) {
      ApplicationLogger.error("[BATCH] Exception in createJob: " + e.getMessage(), e);
      throw new RuntimeException("Failed to create job", e);
    }
  }

  /**
   * Create the main processing step
   *
   * @param inputData Input data for processing
   * @return Configured Spring Batch Step
   */
  protected Step createProcessingStep(List<T> inputData) {
    try {
      ItemProcessor<T, P> userProcessor = createItemProcessor();
      ItemProcessor<T, P> compositeProcessor = createCompositeProcessor(userProcessor);
      ItemWriter<P> userWriter = createItemWriter();
      ItemWriter<P> compositeWriter = createCompositeWriter(userWriter);
      if (userProcessor == null) ApplicationLogger.error("[BATCH] User processor is null!");
      if (userWriter == null) ApplicationLogger.error("[BATCH] Writer is null!");
      return new StepBuilder(jobName + "Step", jobRepository)
          .<T, P>chunk(chunkSize, transactionManager)
          .reader(createItemReader(inputData))
          .processor(compositeProcessor)
          .writer(compositeWriter)
          .faultTolerant()
          .skip(Exception.class) // Skip all exceptions to continue to next chunk
          .skipPolicy(createSkipPolicy())
          .skipLimit(Integer.MAX_VALUE)
          .noRetry(Exception.class) // Don't retry failed items
          .build();
    } catch (Exception e) {
      ApplicationLogger.error("[BATCH] Exception in createProcessingStep: " + e.getMessage(), e);
      throw new RuntimeException("Failed to create step", e);
    }
  }

  /**
   * Creates a composite processor that combines user business logic with automatic modified field
   * handling
   *
   * @param userProcessor The user's business logic processor
   * @return Composite processor that handles both business logic and modified fields
   */
  protected ItemProcessor<T, P> createCompositeProcessor(ItemProcessor<T, P> userProcessor) {
    return item -> {
      P processedItem = userProcessor != null ? userProcessor.process(item) : null;
      // Increment item counter for tracking
      if (processedItem != null) {
        itemCounter.incrementAndGet();
        // Add any automatic field handling here if needed in the future
      }
      return processedItem;
    };
  }

  /**
   * Creates a composite writer that ensures system job flag is set for database operations
   *
   * @param userWriter The user's item writer
   * @return Composite writer that sets system job flag before writing
   */
  protected ItemWriter<P> createCompositeWriter(ItemWriter<P> userWriter) {
    return items -> {
      for (P item : items) {
        if (userWriter != null) {
          Chunk<P> singleItemChunk = new Chunk<>(Collections.singletonList(item));
          userWriter.write(singleItemChunk);
        }
      }
    };
  }

    /**
     * Create item reader from input list
     *
     * @param inputData Input data list
     * @return ItemReader for the data
     */
    protected ItemReader<T> createItemReader(List<T> inputData) {
        return new ItemReader<>() {
            private final ListItemReader<T> delegate = new ListItemReader<>(inputData);

            @Override
            public T read() {
                return delegate.read();
            }
        };
    }

    protected ItemReader<T> createCsvItemReader(FileDTO fileDTO,String[] tokens,Class<?> targetClass) {
        FlatFileItemReader<T> reader = new FlatFileItemReader<T>();

        // Configure tokenizer (set delimiter, names, etc.) based on your CSV structure
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
         tokenizer.setDelimiter(",");
        tokenizer.setNames(tokens);

        DefaultLineMapper<T> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(new RecordFieldSetMapper(targetClass));

        reader.setLineMapper(lineMapper);
        reader.setResource(fileDTO.getResource());
        try {
            reader.afterPropertiesSet();
        } catch (Exception e) {
            ApplicationLogger.error("Failed to initialize CSV reader", e);
            throw new RuntimeException("Failed to initialize CSV reader", e);
        }
        return reader;
    }

  /**
   * Abstract method to create the item processor Implement this to define your business logic Note:
   * You no longer need to call setBatchJobModifiedFields() - it's handled automatically
   *
   * @return ItemProcessor for transforming items
   */
  protected abstract ItemProcessor<T, P> createItemProcessor();

  /**
   * Abstract method to create the item writer Implement this to define how processed items are
   * saved
   *
   * @return ItemWriter for saving processed items
   */
  protected abstract ItemWriter<P> createItemWriter();

  /**
   * Create skip policy for fault-tolerant processing This policy allows the job to continue even
   * when individual items fail
   *
   * @return SkipPolicy that skips all exceptions to allow processing to continue
   */
  protected SkipPolicy createSkipPolicy() {
    return (t, skipCount) -> {
      ApplicationLogger.error(
          "[BATCH] Skipping failed item (skip count: " + skipCount + ") due to: " + t.getMessage());
      return true;
    };
  }

  /**
   * Create batch job record for tracking
   *
   * @param batchJobName Display name
   * @param totalItems Total number of items
   */
  protected void createBatchJobRecord(String batchJobName, int totalItems) {
    batchJobDTO.setName(batchJobName);
    batchJobDTO.setStartTime(DateTimeZoneUtils.getCurrentDate());
    batchJobDTO.setTotalItemCount(totalItems);
    batchJobDTO.setStatus(BatchJob.BatchJobStatus.INITIALIZED);

    batchJobDTO = batchJobService.create(batchJobDTO);
    ApplicationLogger.info(
        "Created batch job record with ID: "
            + batchJobDTO.getId()
            + ", name: "
            + batchJobName
            + ", total items: "
            + totalItems);
  }

  /**
   * Update job status
   *
   * @param status New status
   */
  protected void updateJobStatus(BatchJob.BatchJobStatus status) {
    if (batchJobDTO == null) return;

    try {
      BatchJobDTO updateObject = new BatchJobDTO();
      updateObject.setId(batchJobDTO.getId());
      updateObject.setStatus(status);
      if (status == BatchJob.BatchJobStatus.COMPLETED || status == BatchJob.BatchJobStatus.FAILED) {
        updateObject.setEndTime(DateTimeZoneUtils.getCurrentDate());
      }

      batchJobService.update(updateObject);
      ApplicationLogger.info("Updated batch job " + batchJobDTO.getId() + " status to: " + status);
    } catch (Exception e) {
      ApplicationLogger.error("Failed to update batch job status", e);
    }
  }

  @Override
  public void beforeJob(JobExecution jobExecution) {
    ApplicationLogger.info(
        "Starting Spring Batch job execution: " + jobExecution.getJobInstance().getJobName());

    // Reset chunk number for each job execution
    currentChunkNumber = 0;
    itemCounter.set(0);
    ApplicationLogger.info("Set system job flag to true for batch processing");
    updateJobStatus(BatchJob.BatchJobStatus.RUNNING);
  }

  @Override
  public void afterJob(JobExecution jobExecution) {
    ApplicationLogger.info(
        "Spring Batch job completed: " + jobExecution.getJobInstance().getJobName());

    // Calculate failed count from step executions
    int failedCount = 0;
    int processedCount = 0;

    for (StepExecution stepExecution : jobExecution.getStepExecutions()) {
      failedCount += (int) stepExecution.getSkipCount();
      processedCount += (int) stepExecution.getWriteCount();
    }

    boolean anyFailed = failedCount > 0 || jobExecution.getStatus() == BatchStatus.FAILED;

    if (anyFailed) {
      updateJobStatus(BatchJob.BatchJobStatus.FAILED);
      ApplicationLogger.info("Batch job marked as FAILED due to item failures. Failed count: " + failedCount);
    } else {
      updateJobStatus(BatchJob.BatchJobStatus.COMPLETED);
      ApplicationLogger.info("Batch job marked as COMPLETED (no item failures).");
    }

    // Update final processed count and failed count
    if (batchJobDTO != null) {
      try {
        BatchJobDTO updateObject = new BatchJobDTO();
        updateObject.setId(batchJobDTO.getId());
        updateObject.setProcessedCount(processedCount);
        updateObject.setFailedCount(failedCount);

        batchJobService.update(updateObject);

      } catch (Exception e) {
        ApplicationLogger.error("Failed to update final processed count", e);
      }
    }

    ApplicationLogger.info(
        "Completed Spring Batch job execution: "
            + jobExecution.getJobInstance().getJobName()
            + " with status: "
            + jobExecution.getStatus()
            + ", processed: " + processedCount
            + ", failed: " + failedCount);

    // Send notification
    if (batchJobDTO != null) {
      batchJobService.sendNotificationToUser(batchJobDTO);
    }
  }

  /** Latest execution for a given job name */
  public JobExecution getLatestJobExecution(String jobName) {
    try {
      List<JobInstance> jobInstances = jobExplorer.getJobInstances(jobName, 0, 1);
      if (!jobInstances.isEmpty()) {
        JobInstance latestInstance = jobInstances.get(0);
        List<JobExecution> executions = jobExplorer.getJobExecutions(latestInstance);
        if (!executions.isEmpty()) {
          return executions.get(executions.size() - 1);
        }
      }
    } catch (Exception e) {
      ApplicationLogger.error("Failed to get job execution for: " + jobName, e);
    }
    return null;
  }

  /** Check if a job is running */
  public boolean isJobRunning(String jobName) {
    JobExecution execution = getLatestJobExecution(jobName);
    return execution != null && execution.getStatus().isRunning();
  }

  /** Number of currently running executions for this job */
  public int getRunningJobCount(String jobName) {
    try {
      Set<JobExecution> running = jobExplorer.findRunningJobExecutions(jobName);
      return running.size();
    } catch (Exception e) {
      ApplicationLogger.error("Failed to get running job count", e);
      return 0;
    }
  }

  /** Stop a running job */
  public boolean stopJob(String jobName) {
    JobExecution exec = getLatestJobExecution(jobName);
    if (exec != null && exec.getStatus().isRunning()) {
      try {
        jobOperator.stop(exec.getId());
        ApplicationLogger.info("Stop requested for job: " + jobName);
        return true;
      } catch (Exception e) {
        ApplicationLogger.error("Failed to stop job: " + jobName, e);
      }
    }
    return false;
  }

  /** Restart a failed or stopped job */
  public boolean restartJob(String jobName) {
    JobExecution exec = getLatestJobExecution(jobName);
    if (exec != null
        && (exec.getStatus() == BatchStatus.FAILED || exec.getStatus() == BatchStatus.STOPPED)) {
      try {
        long newExecId = jobOperator.restart(exec.getId());
        ApplicationLogger.info("Restarted job: " + jobName + " new executionId=" + newExecId);
        return true;
      } catch (Exception e) {
        ApplicationLogger.error("Failed to restart job: " + jobName, e);
      }
    }
    return false;
  }

  /** Terminate a job (abandon it) */
  public boolean terminateJob(String jobName) {
    JobExecution exec = getLatestJobExecution(jobName);
    if (exec != null) {
      try {
        jobOperator.abandon(exec.getId());
        ApplicationLogger.info("Job abandoned (terminated): " + jobName);
        return true;
      } catch (Exception e) {
        ApplicationLogger.error("Failed to abandon job: " + jobName, e);
      }
    }
    return false;
  }
}

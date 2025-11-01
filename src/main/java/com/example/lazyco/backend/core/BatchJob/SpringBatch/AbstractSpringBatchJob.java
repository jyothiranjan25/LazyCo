package com.example.lazyco.backend.core.BatchJob.SpringBatch;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.BatchJob.BatchJob;
import com.example.lazyco.backend.core.BatchJob.BatchJobDTO;
import com.example.lazyco.backend.core.BatchJob.BatchJobService;
import com.example.lazyco.backend.core.CsvTemplate.CsvService;
import com.example.lazyco.backend.core.DateUtils.DateTimeZoneUtils;
import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Getter;
import lombok.Setter;
import org.springframework.batch.core.*;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Component
public abstract class AbstractSpringBatchJob<T extends AbstractDTO<?>, P extends AbstractDTO<?>>
    implements JobExecutionListener, StepExecutionListener {

  private JobRepository jobRepository;
  private JobLauncher jobLauncher;
  private PlatformTransactionManager transactionManager;
  private BatchJobService batchJobService;

  @Autowired
  public void injectDependencies(
      JobRepository jobRepository,
      JobLauncher jobLauncher,
      PlatformTransactionManager transactionManager,
      BatchJobService batchJobService) {
    this.jobRepository = jobRepository;
    this.jobLauncher = jobLauncher;
    this.transactionManager = transactionManager;
    this.batchJobService = batchJobService;
  }

  @Setter @Getter protected BatchJobDTO batchJobDTO;

  protected String jobName;
  protected Long jobId;
  protected int chunkSize;

  protected int currentChunkNumber = 0;

  protected AtomicInteger itemCounter = new AtomicInteger(0);

  public AbstractSpringBatchJob() {
    this.jobName = this.getClass().getSimpleName();
    this.chunkSize = 1; // Default chunk size
  }

  public AbstractSpringBatchJob(String jobName, int chunkSize) {
    this.jobName = jobName;
    this.chunkSize = chunkSize;
  }

  @SuppressWarnings("unchecked")
  public void executeJob(T inputData) {
    try {
      List<?> listDate = CsvService.generateCsvToList(inputData.getFile(), inputData.getClass());
      inputData.setObjects(listDate);
      if (inputData.getObjects() != null && !inputData.getObjects().isEmpty()) {
        ApplicationLogger.info(
            "Executing Spring Batch job: "
                + jobName
                + " with input object containing "
                + inputData.getObjects().size()
                + " items");
        this.executeJobInBackground((List<T>) inputData.getObjects(), jobName);
      } else {
        ApplicationLogger.info("No input objects found in the provided DTO for job: " + jobName);
      }
    } catch (Exception e) {
      ApplicationLogger.error("Failed to execute Spring Batch job: " + jobName, e);
      throw new RuntimeException("Batch job execution failed", e);
    }
  }

  private void executeJobInBackground(List<T> inputData, String batchJobName) {
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
              .toJobParameters();
      ApplicationLogger.info(
          "Starting Spring Batch job: " + jobName + " with " + inputData.size() + " items");
      JobExecution jobExecution = jobLauncher.run(job, jobParameters);
      this.jobId = jobExecution.getJobId();
    } catch (Exception e) {
      ApplicationLogger.error("[BATCH] Exception in executeJob: " + e.getMessage(), e);
      if (batchJobDTO != null) {
        updateJobStatus(BatchJob.BatchJobStatus.FAILED);
      }
    }
  }

  protected void createBatchJobRecord(String batchJobName, int totalItems) {
    batchJobDTO.setJobId(this.jobId);
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
          .listener(this)
          .reader(ItemReader(inputData))
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

  protected ItemReader<T> ItemReader(List<T> inputData) {
    return new CheckpointingItemReader<>(inputData, "checkpoint_" + jobName + ".chk");
  }

  protected abstract ItemProcessor<T, P> createItemProcessor();

  protected abstract ItemWriter<P> createItemWriter();

  protected SkipPolicy createSkipPolicy() {
    return (t, skipCount) -> {
      ApplicationLogger.error(
          "[BATCH] Skipping failed item (skip count: " + skipCount + ") due to: " + t.getMessage());
      return true;
    };
  }

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
  public void beforeStep(StepExecution stepExecution) {
    ApplicationLogger.info("Starting Step: " + stepExecution.getStepName());
  }

  @Override
  public ExitStatus afterStep(StepExecution stepExecution) {
    ApplicationLogger.info(
        "Completed Step: "
            + stepExecution.getStepName()
            + " with status: "
            + stepExecution.getStatus());
    return stepExecution.getExitStatus();
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
      ApplicationLogger.info(
          "Batch job marked as FAILED due to item failures. Failed count: " + failedCount);
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
            + ", processed: "
            + processedCount
            + ", failed: "
            + failedCount);

    // Send notification
    if (batchJobDTO != null) {
      batchJobService.sendNotificationToUser(batchJobDTO);
    }
  }

  public static class CheckpointingItemReader<T> implements ItemReader<T> {

    private final List<T> data;
    private int currentIndex = 0;
    private final File checkpointFile;

    public CheckpointingItemReader(List<T> data, String checkpointFilePath) {
      this.data = data;
      this.checkpointFile = new File(checkpointFilePath);
      restoreCheckpoint();
    }

    @Override
    public T read() {
      if (currentIndex >= data.size()) {
        return null; // end of data
      }
      T item = data.get(currentIndex++);
      saveCheckpoint();
      return item;
    }

    private void saveCheckpoint() {
      try (FileWriter fw = new FileWriter(checkpointFile)) {
        fw.write(String.valueOf(currentIndex));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    private void restoreCheckpoint() {
      if (checkpointFile.exists()) {
        try (BufferedReader br = new BufferedReader(new FileReader(checkpointFile))) {
          String line = br.readLine();
          if (line != null && !line.isEmpty()) {
            currentIndex = Integer.parseInt(line);
          }
        } catch (Exception e) {
          currentIndex = 0;
        }
      }
    }

    public void resetCheckpoint() {
      if (checkpointFile.exists()) {
        checkpointFile.delete();
      }
      currentIndex = 0;
    }
  }
}

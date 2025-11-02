package com.example.lazyco.backend.core.BatchJob.SpringBatch;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.BatchJob.BatchJob;
import com.example.lazyco.backend.core.BatchJob.BatchJobDTO;
import com.example.lazyco.backend.core.BatchJob.BatchJobService;
import com.example.lazyco.backend.core.CsvTemplate.CsvService;
import com.example.lazyco.backend.core.DateUtils.DateTimeZoneUtils;
import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import java.util.*;
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
public abstract class AbstractBatchJob<T extends AbstractDTO<?>, P extends AbstractDTO<?>> {

  private JobRepository jobRepository;
  private JobLauncher jobLauncher;
  private AbstractJobListener jobListener;
  private AbstractStepListener stepListener;
  private AbstractChunkListener chunkListener;
  private PlatformTransactionManager transactionManager;
  private BatchJobService batchJobService;

  @Autowired
  public void injectDependencies(
      JobRepository jobRepository,
      JobLauncher jobLauncher,
      AbstractJobListener jobListener,
      AbstractStepListener stepListener,
      AbstractChunkListener chunkListener,
      PlatformTransactionManager transactionManager,
      BatchJobService batchJobService) {
    this.jobRepository = jobRepository;
    this.jobLauncher = jobLauncher;
    this.jobListener = jobListener;
    this.stepListener = stepListener;
    this.chunkListener = chunkListener;
    this.transactionManager = transactionManager;
    this.batchJobService = batchJobService;
  }

  @Setter @Getter protected BatchJobDTO batchJobDTO;

  protected String jobName;

  public AbstractBatchJob() {
    this.jobName = this.getClass().getSimpleName();
  }

  public AbstractBatchJob(String jobName) {
    this.jobName = jobName;
  }

  @SuppressWarnings("unchecked")
  public void executeJob(T inputData) {
    try {
      List<T> listDate =
          (List<T>) CsvService.generateCsvToList(inputData.getFile(), inputData.getClass());
      if (!listDate.isEmpty()) {
        ApplicationLogger.info(
            "Executing Spring Batch job: "
                + jobName
                + " with input object containing "
                + listDate.size()
                + " items");
        this.executeJobInBackground(listDate, jobName);
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

      Job job = createJob(inputData);
      JobParameters jobParameters =
          new JobParametersBuilder()
              .addLong("timestamp", System.currentTimeMillis())
              .toJobParameters();
      ApplicationLogger.info(
          "Starting Spring Batch job: " + jobName + " with " + inputData.size() + " items");
      JobExecution jobExecution = jobLauncher.run(job, jobParameters);
    } catch (Exception e) {
      ApplicationLogger.error("[BATCH] Exception in executeJob: " + e.getMessage(), e);
      if (batchJobDTO != null) {
        updateJobStatus(BatchJob.BatchJobStatus.FAILED);
      }
    }
  }

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

  protected Job createJob(List<T> inputData) {
    try {
      return new JobBuilder(jobName, jobRepository)
          .listener(jobListener)
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
          .<T, P>chunk(1, transactionManager)
          .listener(stepListener)
          .listener(chunkListener)
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
      return userProcessor != null ? userProcessor.process(item) : null;
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
    AbstractItemReader<T> reader =
        new AbstractItemReader<>(inputData, "checkpoint_" + jobName + ".chk");
    reader.resetCheckpoint(); // 👈 add this
    return reader;
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
}

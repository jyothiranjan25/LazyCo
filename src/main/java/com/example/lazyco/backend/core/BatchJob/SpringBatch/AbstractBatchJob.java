package com.example.lazyco.backend.core.BatchJob.SpringBatch;

import com.example.lazyco.backend.core.AbstractAction;
import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.CsvTemplate.CsvService;
import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import com.example.lazyco.backend.core.Utils.CommonConstants;
import java.util.*;
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
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Component
@Scope("prototype")
public abstract class AbstractBatchJob<T extends AbstractDTO<?>, P extends AbstractDTO<?>> {

  private JobRepository jobRepository;
  private JobLauncher jobLauncher;
  private PlatformTransactionManager transactionManager;
  private AbstractJobListener jobListener;
  private AbstractStepListener stepListener;
  private AbstractChunkListener chunkListener;
  private CsvService csvService;
  private AbstractAction abstractAction;

  @Autowired
  public void injectDependencies(
      JobRepository jobRepository,
      JobLauncher jobLauncher,
      PlatformTransactionManager transactionManager,
      AbstractJobListener jobListener,
      AbstractStepListener stepListener,
      AbstractChunkListener chunkListener,
      CsvService csvService,
      AbstractAction abstractAction) {
    this.jobRepository = jobRepository;
    this.jobLauncher = jobLauncher;
    this.transactionManager = transactionManager;
    this.jobListener = jobListener;
    this.stepListener = stepListener;
    this.chunkListener = chunkListener;
    this.csvService = csvService;
    this.abstractAction = abstractAction;
  }

  @SuppressWarnings("unchecked")
  public void executeJob(T inputData) {
    try {
      List<T> listDate =
          (List<T>) csvService.generateCsvToList(inputData.getFile(), inputData.getClass());
      String uniqueJobName =
          this.getClass().getSimpleName() + "_" + UUID.randomUUID(); // 👈 unique per run
      if (!listDate.isEmpty()) {
        ApplicationLogger.info(
            "Executing Spring Batch job: "
                + uniqueJobName
                + " with input object containing "
                + listDate.size()
                + " items");
        this.executeJobInBackground(listDate, uniqueJobName);
      } else {
        ApplicationLogger.info(
            "No input objects found in the provided DTO for job: "
                + this.getClass().getSimpleName());
      }
    } catch (Exception e) {
      ApplicationLogger.error(
          "Failed to execute Spring Batch job: " + this.getClass().getSimpleName(), e);
      throw new RuntimeException("Batch job execution failed", e);
    }
  }

  private void executeJobInBackground(List<T> inputData, String batchJobName) {
    try {
      Job job = createJob(inputData, batchJobName);
      JobParameters jobParameters =
          new JobParametersBuilder()
              .addString("batchJobName", batchJobName)
              .addLong("timestamp", System.currentTimeMillis())
              .addLong(CommonConstants.LOGGED_USER, abstractAction.getLoggedInUser().getId())
              .addLong(
                  CommonConstants.LOGGED_USER_ROLE, abstractAction.getLoggedInUserRole().getId())
              .toJobParameters();
      ApplicationLogger.info(
          "Starting Spring Batch job: " + batchJobName + " with " + inputData.size() + " items");
      JobExecution jobExecution = jobLauncher.run(job, jobParameters);
    } catch (Exception e) {
      ApplicationLogger.error("[BATCH] Exception in executeJob: " + e.getMessage(), e);
    }
  }

  protected Job createJob(List<T> inputData, String jobName) {
    try {
      return new JobBuilder(jobName, jobRepository)
          .listener(jobListener)
          .start(createProcessingStep(inputData, jobName))
          .build();
    } catch (Exception e) {
      ApplicationLogger.error("[BATCH] Exception in createJob: " + e.getMessage(), e);
      throw new RuntimeException("Failed to create job", e);
    }
  }

  protected Step createProcessingStep(List<T> inputData, String jobName) {
    try {
      ItemProcessor<T, P> userProcessor = createItemProcessor();
      ItemProcessor<T, P> compositeProcessor = createCompositeProcessor(userProcessor);
      ItemWriter<P> userWriter = createItemWriter();
      ItemWriter<P> compositeWriter = createCompositeWriter(userWriter);
      if (userProcessor == null) ApplicationLogger.error("[BATCH] User processor is null!");
      if (userWriter == null) ApplicationLogger.error("[BATCH] Writer is null!");
      return new StepBuilder(jobName + "_Step", jobRepository)
          .<T, P>chunk(1, transactionManager)
          .listener(stepListener)
          .listener(chunkListener)
          .reader(ItemReader(inputData, jobName))
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

  protected ItemReader<T> ItemReader(List<T> inputData, String jobName) {
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
}

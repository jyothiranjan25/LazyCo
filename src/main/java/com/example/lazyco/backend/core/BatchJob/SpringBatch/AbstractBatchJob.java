package com.example.lazyco.backend.core.BatchJob.SpringBatch;

import com.example.lazyco.backend.core.AbstractAction;
import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.BatchJob.*;
import com.example.lazyco.backend.core.DateUtils.DateTimeZoneUtils;
import com.example.lazyco.backend.core.File.FileTypeEnum;
import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import com.example.lazyco.backend.core.Utils.CommonConstants;
import java.util.*;
import org.springframework.batch.core.*;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.listener.*;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.batch.infrastructure.item.ItemStream;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public abstract class AbstractBatchJob<T extends AbstractDTO<?>, P extends AbstractDTO<?>> {

  private JobRepository jobRepository;
  private JobOperator jobOperator;
  private ObjectProvider<AbstractBatchJobListener<T, P>> batchJobListeners;
  private BatchJobService batchJobService;
  private AbstractAction abstractAction;

  @Autowired
  public void injectDependencies(
      JobRepository jobRepository,
      JobOperator jobOperator,
      ObjectProvider<AbstractBatchJobListener<T, P>> batchJobListeners,
      BatchJobService batchJobService,
      AbstractAction abstractAction) {
    this.jobRepository = jobRepository;
    this.jobOperator = jobOperator;
    this.batchJobListeners = batchJobListeners;
    this.batchJobService = batchJobService;
    this.abstractAction = abstractAction;
  }

  public void executeJob(T inputData) {
    executeJob(inputData, Collections.emptyMap());
  }

  @SuppressWarnings("unchecked")
  public void executeJob(T inputData, Map<Class<?>, ?> childData) {
    try {
      List<T> listDate = (List<T>) inputData.getObjects();
      String uniqueJobName =
          this.getClass().getSimpleName() + "_" + UUID.randomUUID(); // 👈 unique per run
      if (!listDate.isEmpty()) {
        ApplicationLogger.info(
            "Executing Spring Batch job: "
                + uniqueJobName
                + " with input object containing "
                + listDate.size()
                + " items");
        // determine if notification is to be sent
        boolean sendNotification = Boolean.TRUE.equals(inputData.getSendNotification());
        // determine operation type
        BatchJobOperationType actionType =
            inputData.getOperationType() != null
                ? inputData.getOperationType()
                : BatchJobOperationType.CREATE;
        this.executeJobInBackground(
            listDate, childData, uniqueJobName, actionType, sendNotification);
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

  private void executeJobInBackground(
      List<T> inputData,
      Map<Class<?>, ?> childData,
      String batchJobName,
      BatchJobOperationType operationType,
      boolean sendNotification) {
    // create BatchJob entry
    BatchJobDTO batchJobDTO =
        createBatchJob(batchJobName, inputData.size(), operationType, sendNotification);
    try {
      // create and launch job
      Job job = createJob(inputData, childData, batchJobName, operationType);
      // add batchJobId and logged-in user details to job parameters
      JobParameters jobParameters =
          new JobParametersBuilder()
              .addLong(CommonConstants.BATCH_JOB_ID, batchJobDTO.getId())
              .addString(CommonConstants.BATCH_JOB_NAME, batchJobName)
              .addString(CommonConstants.BATCH_JOB_FILE_PATH, batchJobDTO.getOutputFilePath())
              .addLong(CommonConstants.LOGGED_USER, abstractAction.getLoggedInUser().getId())
              .addLong(
                  CommonConstants.LOGGED_USER_ROLE, abstractAction.getLoggedInUserRole().getId())
              .addLong(CommonConstants.Batch_JOB_TIME_STAMP, System.currentTimeMillis())
              .toJobParameters();
      JobExecution jobExecution = jobOperator.start(job, jobParameters);
      ApplicationLogger.info(
          "Starting Spring Batch job: "
              + batchJobName
              + " with "
              + inputData.size()
              + " items to process. JobExecution ID: "
              + jobExecution.getId());
    } catch (Exception e) {
      ApplicationLogger.error("[BATCH] Exception in executeJob: " + e.getMessage(), e);
      // update BatchJob entry as FAILED
      updateBatchJob(batchJobDTO);
    }
  }

  protected Job createJob(
      List<T> inputData,
      Map<Class<?>, ?> childData,
      String jobName,
      BatchJobOperationType operationType) {
    try {
      // use getBean to ensure new instance of listener per job
      AbstractBatchJobListener jobListener = batchJobListeners.getObject();
      return new JobBuilder(jobName, jobRepository)
          .listener((JobExecutionListener) jobListener)
          .start(createProcessingStep(inputData, childData, jobName, jobListener, operationType))
          .build();
    } catch (Exception e) {
      ApplicationLogger.error("[BATCH] Exception in createJob: " + e.getMessage(), e);
      throw new RuntimeException("Failed to create job", e);
    }
  }

  protected Step createProcessingStep(
      List<T> inputData,
      Map<Class<?>, ?> childData,
      String jobName,
      AbstractBatchJobListener jobListener,
      BatchJobOperationType operationType) {
    try {
      //  create reader
      ItemReader<T> reader = ItemReader(inputData, jobName);
      // create processor
      ItemProcessor<T, P> userProcessor = createItemProcessor(operationType, childData);
      ItemProcessor<T, P> compositeProcessor = createCompositeProcessor(userProcessor);
      // create writer
      ItemWriter<P> userWriter = createItemWriter(operationType);
      ItemWriter<P> compositeWriter = createCompositeWriter(userWriter);
      return new StepBuilder(jobName + "_Step", jobRepository)
              .<T, P>chunk(3)
              .listener((StepExecutionListener) jobListener)
              .listener((ChunkListener<T, P>) jobListener)
              .listener((ItemProcessListener<T, P>) jobListener)
              .listener((ItemWriteListener<P>) jobListener)
              .listener((SkipListener<T, P>) jobListener)
              .reader(reader)
              .stream((ItemStream) reader)
              .processor(compositeProcessor)
              .writer(compositeWriter)
              .faultTolerant()
              .skip(Exception.class) // Skip all exceptions to continue to next chunk
              .skipPolicy(createSkipPolicy())
              .skipLimit(Integer.MAX_VALUE)
              .build();
    } catch (Exception e) {
      ApplicationLogger.error("[BATCH] Exception in createProcessingStep: " + e.getMessage(), e);
      throw new RuntimeException("Failed to create step", e);
    }
  }

  protected <Z> ItemProcessor<T, P> createCompositeProcessor(ItemProcessor<T, P> userProcessor) {
    return item -> {
      return userProcessor != null ? userProcessor.process(item) : null;
    };
  }

  protected ItemWriter<P> createCompositeWriter(ItemWriter<P> userWriter) {
    return items -> {
      //      for (P item : items) {
      //        if (userWriter != null) {
      //          Chunk<P> singleItemChunk = new Chunk<>(Collections.singletonList(item));
      //          userWriter.write(singleItemChunk);
      //        }
      //      }
      if (userWriter != null) {
        userWriter.write(items);
      }
    };
  }

  protected ItemReader<T> ItemReader(List<T> inputData, String jobName) {
    return new AbstractItemReader<>(inputData, jobName);
  }

  protected abstract ItemProcessor<T, P> createItemProcessor(
      BatchJobOperationType operationType, Map<Class<?>, ?> childData);

  protected abstract ItemWriter<P> createItemWriter(BatchJobOperationType operationType);

  protected SkipPolicy createSkipPolicy() {
    return (t, skipCount) -> {
      ApplicationLogger.error(
          "[BATCH] Skipping failed item (skip count: " + skipCount + ") due to: " + t.getMessage());
      return true;
    };
  }

  private BatchJobDTO createBatchJob(
      String jobName,
      int totalItemCount,
      BatchJobOperationType operationType,
      boolean sendNotification) {
    BatchJobDTO batchJobDTO = new BatchJobDTO();
    batchJobDTO.setName(jobName);
    batchJobDTO.setOperationType(operationType);
    batchJobDTO.setStartTime(DateTimeZoneUtils.getCurrentDate());
    batchJobDTO.setTotalItemCount(totalItemCount);
    batchJobDTO.setStatus(BatchJobStatus.INITIALIZED);
    batchJobDTO.setSessionType(BatchJobSessionType.SINGLE_OBJECT_COMMIT);
    batchJobDTO.setOutputFilePath(
        CommonConstants.BATCH_JOB_UPLOAD_LOCATION + jobName + FileTypeEnum.CSV.getExtension());
    batchJobDTO.setNotifyOnCompletion(sendNotification);
    batchJobDTO = batchJobService.create(batchJobDTO);
    return batchJobDTO;
  }

  private void updateBatchJob(BatchJobDTO batchJobDTO) {
    batchJobDTO.setEndTime(DateTimeZoneUtils.getCurrentDate());
    batchJobDTO.setProcessedCount(0);
    batchJobDTO.setFailedCount(0);
    batchJobDTO.setStatus(BatchJobStatus.FAILED);
    batchJobService.update(batchJobDTO);
  }
}

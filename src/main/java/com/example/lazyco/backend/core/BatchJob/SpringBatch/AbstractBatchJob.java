package com.example.lazyco.backend.core.BatchJob.SpringBatch;

import com.example.lazyco.backend.core.AbstractAction;
import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.BatchJob.*;
import com.example.lazyco.backend.core.DateUtils.DateTimeZoneUtils;
import com.example.lazyco.backend.core.Exceptions.ExceptionWrapper;
import com.example.lazyco.backend.core.File.FileTypeEnum;
import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import com.example.lazyco.backend.core.Utils.CommonConstants;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.jspecify.annotations.NonNull;
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
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.support.ListItemReader;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Component
@Scope("prototype")
public abstract class AbstractBatchJob<I extends AbstractBatchDTO<I>, O extends AbstractDTO<O>> {

  private JobRepository jobRepository;
  private JobOperator jobOperator;
  private ObjectProvider<@NonNull AbstractBatchJobListener<I, O>> jobListener;
  private PlatformTransactionManager transactionManager;
  private BatchJobService batchJobService;
  private AbstractAction abstractAction;

  @Autowired
  public void injectDependencies(
      JobRepository jobRepository,
      JobOperator jobOperator,
      ObjectProvider<@NonNull AbstractBatchJobListener<I, O>> jobListener,
      PlatformTransactionManager transactionManager,
      BatchJobService batchJobService,
      AbstractAction abstractAction) {
    this.jobRepository = jobRepository;
    this.jobOperator = jobOperator;
    this.jobListener = jobListener;
    this.transactionManager = transactionManager;
    this.batchJobService = batchJobService;
    this.abstractAction = abstractAction;
  }

  @Async
  public void executeJob(I inputData) {
    try {
      String jobName = this.getClass().getSimpleName() + "_" + UUID.randomUUID();
      if (!inputData.getObjects().isEmpty()) {
        ApplicationLogger.info(
            "Executing Spring Batch job: "
                + jobName
                + " with input object containing "
                + inputData.getObjects().size()
                + " items");
        executeJobInBackground(inputData, jobName);
      } else {
        throw new ExceptionWrapper(
            "No input objects found in the provided DTO for job: "
                + this.getClass().getSimpleName());
      }
    } catch (ExceptionWrapper ex) {
      throw ex;
    } catch (Exception e) {
      throw new ExceptionWrapper(
          "Failed to execute batch job: " + this.getClass().getSimpleName(), e);
    }
  }

  private void executeJobInBackground(I inputData, String batchJobName) {
    // create BatchJob entry
    BatchJobDTO batchJobDTO = createBatchJob(inputData, batchJobName);
    try {
      // create and launch job
      Job job = createJob(inputData, batchJobName);
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
          "Launched Spring Batch job: "
              + batchJobName
              + " with JobExecutionId: "
              + jobExecution.getId());
    } catch (Exception e) {
      updateBatchJob(batchJobDTO);
      throw new ExceptionWrapper(
          "Failed to execute batch job: " + this.getClass().getSimpleName(), e);
    }
  }

  private Job createJob(I inputData, String jobName) {
    AbstractBatchJobListener<I, O> listener = jobListener.getObject();
    return new JobBuilder(jobName, jobRepository)
        .listener(listener)
        .start(createProcessingStep(inputData, jobName, listener))
        .build();
  }

  private Step createProcessingStep(
      I inputData, String jobName, AbstractBatchJobListener<I, O> listener) {
    try {
      // extract input data
      List<I> inputDataList = inputData.getObjects();
      Map<Class<?>, List<?>> childDataMap = inputData.getChildDataMap();
      BatchJobOperationType operationType = inputData.getOperationType();
      boolean isAtomicOperation = Boolean.TRUE.equals(inputData.getIsAtomicOperation());
      isAtomicOperation = true;
      int chunkSize = isAtomicOperation ? inputDataList.size() : 1;

      //  create reader
      ItemReader<@NonNull I> itemReader = createItemReader(inputDataList);
      // create Processor
      ItemProcessor<@NonNull I, @NonNull O> itemProcessor =
          createItemProcessor(operationType, childDataMap);
      ItemProcessor<@NonNull I, @NonNull O> compositeProcessor =
          createCompositeProcessor(itemProcessor);
      // create writer
      ItemWriter<@NonNull O> itemWriter = createItemWriter(operationType);
      ItemWriter<@NonNull O> compositeWriter = createCompositeWriter(itemWriter, isAtomicOperation);

      return new StepBuilder(jobName + "_ProcessingStep", jobRepository)
          .<I, O>chunk(chunkSize)
          .transactionManager(transactionManager)
          .taskExecutor(new SimpleAsyncTaskExecutor())
          .listener((StepExecutionListener) listener)
          .listener((ChunkListener) listener)
          .listener((ItemReadListener<I>) listener)
          .listener((ItemProcessListener<I, O>) listener)
          .listener((ItemWriteListener<O>) listener)
          .listener((SkipListener<I, O>) listener)
          .reader(itemReader)
          .processor(compositeProcessor)
          .writer(compositeWriter)
          .faultTolerant()
          .skipPolicy(createSkipPolicy((long) inputDataList.size()))
          .skipLimit(Integer.MAX_VALUE)
          .build();
    } catch (Exception e) {
      throw new ExceptionWrapper("Failed to create processing step", e);
    }
  }

  private ItemReader<@NonNull I> createItemReader(List<I> inputData) {
    return new ItemReader<>() {
      private final ListItemReader<@NonNull I> delegate = new ListItemReader<>(inputData);

      @Override
      public I read() {
        return delegate.read();
      }
    };
  }

  protected abstract ItemProcessor<@NonNull I, @NonNull O> createItemProcessor(
      BatchJobOperationType operationType, Map<Class<?>, List<?>> childData);

  private ItemProcessor<@NonNull I, @NonNull O> createCompositeProcessor(
      ItemProcessor<@NonNull I, @NonNull O> userProcessor) {
    return userProcessor;
  }

  protected abstract ItemWriter<@NonNull O> createItemWriter(BatchJobOperationType operationType);

  private ItemWriter<@NonNull O> createCompositeWriter(
      ItemWriter<@NonNull O> userWriter, boolean isAtomicOperation) {
    return items -> {
      boolean rollbackOnFailure = false;
      for (O item : items) {
        try {
          userWriter.write(new Chunk<>(item));
        } catch (Exception e) {
          rollbackOnFailure = true;
        }
      }
      if (isAtomicOperation && rollbackOnFailure) {
        throw new ExceptionWrapper(
            "One or more items failed to process in the chunk, rolling back the transaction");
      }
    };
  }

  private SkipPolicy createSkipPolicy(Long size) {
    return (throwable, skipCount) -> {
      ApplicationLogger.error(throwable);
      return true;
    };
  }

  private BatchJobDTO createBatchJob(I inputDate, String jobName) {
    BatchJobDTO batchJobDTO = new BatchJobDTO();
    batchJobDTO.setName(jobName);
    batchJobDTO.setOperationType(inputDate.getOperationType());
    batchJobDTO.setStartTime(DateTimeZoneUtils.getCurrentDate());
    batchJobDTO.setTotalItemCount(inputDate.getObjects().size());
    batchJobDTO.setStatus(BatchJobStatus.INITIALIZED);
    batchJobDTO.setSessionType(BatchJobSessionType.SINGLE_OBJECT_COMMIT);
    batchJobDTO.setOutputFilePath(
        CommonConstants.BATCH_JOB_UPLOAD_LOCATION + jobName + FileTypeEnum.CSV.getExtension());
    batchJobDTO.setNotifyOnCompletion(inputDate.getSendNotification());
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

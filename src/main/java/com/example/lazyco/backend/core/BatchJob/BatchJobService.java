package com.example.lazyco.backend.core.BatchJob;

import com.example.lazyco.backend.core.AbstractClasses.Service.AbstractService;
import com.example.lazyco.backend.core.BatchJob.SpringBatch.SpringBatchAction;
import com.example.lazyco.backend.core.Exceptions.ApplicationException;
import com.example.lazyco.backend.core.Exceptions.CommonMessage;
import com.example.lazyco.backend.core.Exceptions.ExceptionWrapper;
import com.example.lazyco.backend.core.File.FileDTO;
import com.example.lazyco.backend.core.Utils.CommonConstants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BatchJobService extends AbstractService<BatchJobDTO, BatchJob>
    implements IBatchJobService {

  private final SpringBatchAction springBatchAction;

  public BatchJobService(BatchJobMapper batchJobMapper, SpringBatchAction springBatchAction) {
    super(batchJobMapper);
    this.springBatchAction = springBatchAction;
  }

  public void makeUpdates(BatchJobDTO updateObject, BatchJob existing) {
    if (updateObject.getStatus() != null) {
      existing.setStatus(updateObject.getStatus());
    }

    if (updateObject.getJobId() != null) {
      existing.setJobId(updateObject.getJobId());
    }

    if (updateObject.getProcessedCount() != null) {
      existing.setProcessedCount(updateObject.getProcessedCount());
    }

    if (updateObject.getFailedCount() != null) {
      existing.setFailedCount(updateObject.getFailedCount());
    }

    if (updateObject.getEndTime() != null) {
      existing.setEndTime(updateObject.getEndTime());
    }
  }

  public BatchJobDTO terminateJob(BatchJobDTO batchJobDTO) {
    BatchJobDTO proxy = new BatchJobDTO();
    proxy.setId(batchJobDTO.getId());
    proxy.setStatus(BatchJob.BatchJobStatus.TERMINATED);
    return update(proxy);
  }

  @Override
  public BatchJobDTO getByJobId(Long jobId) {
    BatchJobDTO filter = new BatchJobDTO();
    filter.setJobId(jobId);
    return getSingle(filter);
  }

  @Override
  public BatchJobDTO restartJob(BatchJobDTO batchJobDTO) {
    try {
      if (batchJobDTO == null || batchJobDTO.getId() == null) {
        throw new ApplicationException(CommonMessage.ID_REQUIRED);
      }
      BatchJob batchJob = getEntityById(batchJobDTO.getId());

      if (batchJob.isActive()) {
        throw new ExceptionWrapper("Active batch job cannot be restarted.");
      }
      if (!springBatchAction.isJobRunning(batchJob.getJobId())) {
        springBatchAction.restartJob(batchJob.getJobId());
      }
    } catch (Exception e) {
      throw new ExceptionWrapper("Failed to restart batch job.", e);
    }
    return batchJobDTO;
  }

  @Override
  public void sendNotificationToUser(BatchJobDTO batchJob) {
    // Minimal implementation - can be enhanced later if needed
    System.out.println("Batch job notification sent for: " + batchJob.getName());
  }

  @Override
  public FileDTO getCsvAuditFileForJob(Long batchJobId) {
    if (batchJobId == null) {
      throw new ApplicationException(CommonMessage.ID_REQUIRED);
    }

    // Return null as this functionality is no longer needed
    BatchJobDTO batchJob = getById(batchJobId);
    if (batchJob == null) {
      return null;
    }
    String fullPath = CommonConstants.TOMCAT_HOME + batchJob.getOutputFilePath();
    return new FileDTO(fullPath);
  }
}

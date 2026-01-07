package com.example.lazyco.core.BatchJob;

import com.example.lazyco.core.AbstractClasses.Service.IAbstractService;
import com.example.lazyco.core.File.FileDTO;

public interface IBatchJobService extends IAbstractService<BatchJobDTO, BatchJob> {
  BatchJobDTO getByJobId(Long jobId);

  boolean sendNotificationToUser(BatchJobDTO batchJob);

  FileDTO getCsvAuditFileForJob(Long batchJobId);
}

package com.example.lazyco.backend.core.BatchJob;

import com.example.lazyco.backend.core.AbstractClasses.Service.IAbstractService;
import com.example.lazyco.backend.core.File.FileDTO;

public interface IBatchJobService extends IAbstractService<BatchJobDTO, BatchJob> {
  BatchJobDTO getByJobId(Long jobId);

  boolean sendNotificationToUser(BatchJobDTO batchJob);

  FileDTO getCsvAuditFileForJob(Long batchJobId);
}

package com.example.lazyco.backend.core.BatchJob;

import com.example.lazyco.backend.core.AbstractClasses.Service.IAbstractService;
import com.example.lazyco.backend.core.File.FileDTO;
import java.util.List;

public interface IBatchJobService extends IAbstractService<BatchJobDTO, BatchJob> {
  BatchJobDTO getByJobId(Long jobId);

  BatchJobDTO terminateJob(BatchJobDTO batchJobDTO);

  BatchJobDTO restartJob(BatchJobDTO batchJobDTO);

  void sendNotificationToUser(BatchJobDTO batchJob);

  BatchJobDTO update(BatchJobDTO batchJobDTO);

  List<BatchJobDTO> get(BatchJobDTO batchJobDTO);

  FileDTO getCsvAuditFileForJob(Long batchJobId);
}

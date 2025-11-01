package com.example.lazyco.backend.core.BatchJob;

import com.example.lazyco.backend.core.AbstractClasses.Service.IAbstractService;
import com.example.lazyco.backend.core.File.FileDTO;
import java.util.List;

public interface IBatchJobService extends IAbstractService<BatchJobDTO, BatchJob> {
  BatchJobDTO getByJobThreadName(String jobThreadName);

  BatchJobDTO terminateJob(BatchJobDTO batchJobDTO);

  BatchJobDTO restartJob(BatchJobDTO batchJobDTO);

  BatchJobDTO pauseJob(BatchJobDTO batchJobDTO);

  void sendNotificationToUser(BatchJobDTO batchJob);

  BatchJobDTO update(BatchJobDTO batchJobDTO);

  List<BatchJobDTO> get(BatchJobDTO batchJobDTO);

  FileDTO getCsvAuditFileForJob(Long batchJobId);
}

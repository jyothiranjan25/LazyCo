package com.example.lazyco.backend.core.BatchJob;

import static com.example.lazyco.backend.core.Utils.CommonConstrains.BATCH_AUDIT_UPLOAD_LOCATION;

import com.example.lazyco.backend.core.AbstractClasses.Service.AbstractService;
import com.example.lazyco.backend.core.Exceptions.ApplicationExemption;
import com.example.lazyco.backend.core.Exceptions.CommonMessage;
import com.example.lazyco.backend.core.File.FileDTO;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Service;

@Service
public class BatchJobService extends AbstractService<BatchJobDTO, BatchJob>
    implements IBatchJobService {

  public BatchJobService(BatchJobMapper batchJobMapper) {
    super(batchJobMapper);
  }

  public void makeUpdates(BatchJobDTO updateObject, BatchJob existing) {
    if (updateObject.getStatus() != null) {
      existing.setStatus(updateObject.getStatus());
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
  public BatchJobDTO getByJobThreadName(String jobThreadName) {
    BatchJobDTO filter = new BatchJobDTO();
    filter.setJobThreadName(jobThreadName);
    return getSingle(filter);
  }

  @Override
  public BatchJobDTO restartJob(BatchJobDTO batchJobDTO) {
    // TODO implement restart logic later!!!
    return null;
  }

  @Override
  public void sendNotificationToUser(BatchJobDTO batchJob) {
    // Minimal implementation - can be enhanced later if needed
    System.out.println("Batch job notification sent for: " + batchJob.getName());
  }

  @Override
  public FileDTO getCsvAuditFileForJob(Long batchJobId) {
    // Return null as this functionality is no longer needed
    BatchJobDTO batchJob = getById(batchJobId);
    if (batchJob == null) {
      return null;
    }
    String startDate =
        batchJob.getStartTime() != null
            ? batchJob
                .getStartTime()
                .toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            : LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    String fileName = "batch_audit_" + batchJobId + "_" + startDate + ".csv";
    String csvFilePath = Paths.get(BATCH_AUDIT_UPLOAD_LOCATION, fileName).toString();
    Path filePath = Paths.get(csvFilePath);
    if (!Files.exists(filePath)) {
      throw new ApplicationExemption(CommonMessage.RECORD_NOT_FOUND);
    }
    return new FileDTO(filePath.toString());
  }
}

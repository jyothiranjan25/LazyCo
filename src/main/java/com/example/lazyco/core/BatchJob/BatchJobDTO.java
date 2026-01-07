package com.example.lazyco.core.BatchJob;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = BatchJob.class)
public class BatchJobDTO extends AbstractDTO<BatchJobDTO> {

  private Long jobId;
  private String name;
  private Date startTime;
  private Date endTime;
  private Integer totalItemCount;
  private Integer processedCount;
  private Integer failedCount;
  private BatchJobOperationType operationType;
  private BatchJobStatus status;
  private BatchJobSessionType sessionType;
  private String outputFilePath;
  private Boolean notifyOnCompletion;
  private NotifyStatus notifyStatus;

  public enum APIAction {
    TERMINATE,
    RESTART,
    PAUSE,
    RESUME
  }
}

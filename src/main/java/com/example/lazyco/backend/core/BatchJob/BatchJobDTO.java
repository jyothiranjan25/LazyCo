package com.example.lazyco.backend.core.BatchJob;

import com.example.lazyco.backend.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
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
  private BatchJob.BatchJobStatus status;
  private BatchJob.BatchJobSessionType sessionType;
  private String outputFilePath;

  public enum APIAction {
    TERMINATE,
    RESTART,
    PAUSE,
    RESUME
  }
}

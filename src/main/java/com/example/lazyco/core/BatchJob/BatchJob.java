package com.example.lazyco.core.BatchJob;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractRBACModel;
import jakarta.persistence.*;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Getter
@Setter
@Entity
@Table(name = "batch_job")
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BatchJob extends AbstractRBACModel {

  @Column(name = "job_id")
  private Long jobId;

  @Column(name = "name")
  private String name;

  @Column(name = "start_time")
  private Date startTime;

  @Column(name = "end_time")
  private Date endTime;

  @Column(name = "total_item_count")
  private Integer totalItemCount;

  @Column(name = "processed_count")
  private Integer processedCount;

  @Column(name = "failed_count")
  private Integer failedCount;

  @Column(name = "operation_type")
  @Enumerated(EnumType.STRING)
  private BatchJobOperationType operationType;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private BatchJobStatus status;

  @Column(name = "session_type")
  @Enumerated(EnumType.STRING)
  private BatchJobSessionType sessionType;

  @Column(name = "output_file_path", length = 2048)
  private String outputFilePath;

  @Column(name = "notify_on_completion")
  private Boolean notifyOnCompletion;

  @Column(name = "notify_status")
  private NotifyStatus notifyStatus;
}

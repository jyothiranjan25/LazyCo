package com.example.lazyco.backend.core.BatchJob;

import com.example.lazyco.backend.core.AbstractClasses.Entity.AbstractRBACModel;
import jakarta.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
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

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private BatchJobStatus status;

  @Column(name = "session_type")
  @Enumerated(EnumType.STRING)
  private BatchJobSessionType sessionType;

  @Column(name = "output_file_path", length = 2048)
  private String outputFilePath;

  public enum BatchJobStatus {
    INITIALIZED("STARTING"),
    RUNNING("STARTED"),
    COMPLETED("COMPLETED"),
    FAILED("FAILED"),
    PAUSED("STOPPED,STOPPING"),
    RESTARTED("STARTED"),
    TERMINATED("ABANDONED"),
    ;
    @Getter private final String batchJobStatus;

    BatchJobStatus(String batchJobStatus) {
      this.batchJobStatus = batchJobStatus;
    }

    public static Set<BatchJobStatus> getInturruptedStatusSet() {
      Set<BatchJobStatus> set = new HashSet<>();
      set.add(PAUSED);
      set.add(TERMINATED);
      return set;
    }

    public static Set<BatchJobStatus> getCompletedStatusSet() {
      Set<BatchJobStatus> set = new HashSet<>();
      set.add(COMPLETED);
      set.add(FAILED);
      set.add(TERMINATED);
      return set;
    }

    public static Set<BatchJobStatus> getActiveStatusSet() {
      Set<BatchJobStatus> set = new HashSet<>();
      set.add(INITIALIZED);
      set.add(RUNNING);
      set.add(RESTARTED);
      return set;
    }
  }

  @Getter
  public enum BatchJobSessionType {
    SINGLE_OBJECT_COMMIT("Process items one by one"),
    ATOMIC_OPERATION("Process all items in single transaction");

    private final String description;

    BatchJobSessionType(String description) {
      this.description = description;
    }
  }

  public boolean isActive() {
    return BatchJobStatus.getActiveStatusSet().contains(this.status);
  }

  public boolean isCompleted() {
    return BatchJobStatus.getCompletedStatusSet().contains(this.status);
  }

  public boolean isInterrupted() {
    return BatchJobStatus.getInturruptedStatusSet().contains(this.status);
  }

  public double getProgressPercentage() {
    if (totalItemCount == null || totalItemCount == 0) {
      return 0.0;
    }
    int processed = processedCount != null ? processedCount : 0;
    return (double) processed / totalItemCount * 100;
  }

  public int getRemainingCount() {
    if (totalItemCount == null) return 0;
    int processed = processedCount != null ? processedCount : 0;
    int failed = failedCount != null ? failedCount : 0;
    return totalItemCount - processed - failed;
  }
}

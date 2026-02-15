package com.example.lazyco.core.QuartzScheduler.CronJob.CronJobExecutionLog;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractModel;
import com.example.lazyco.core.QuartzScheduler.CronJob.CronJobSchedule.CronJobSchedule;
import jakarta.persistence.*;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import org.hibernate.envers.Audited;

@Audited
@Getter
@Setter
@Entity
@DynamicInsert
@Table(
    name = "cron_job_execution_log",
    comment = "Table storing cron job execution logs",
    indexes = {
      @Index(
          name = "idx_cron_job_execution_log_cron_job_schedule_id",
          columnList = "cron_job_schedule_id"),
      @Index(name = "idx_cron_job_execution_log_status", columnList = "status"),
      @Index(name = "idx_cron_job_execution_log_start_time", columnList = "start_time"),
      @Index(name = "idx_cron_job_execution_log_end_time", columnList = "end_time")
    })
@EntityListeners(CronJobExecutionLogListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CronJobExecutionLog extends AbstractModel {

  @Column(name = "start_time")
  private Date startTime;

  @Column(name = "end_time")
  private Date endTime;

  @Enumerated(EnumType.STRING)
  private CronJobStatusEnum status;

  private String remarks;

  @ManyToOne
  @JoinColumn(
      name = "cron_job_schedule_id",
      foreignKey = @ForeignKey(name = "fk_cron_job_execution_log_cron_job_schedule"),
      nullable = false,
      comment = "Reference to the cron job schedule")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private CronJobSchedule cronJobSchedule;
}

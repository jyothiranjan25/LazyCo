package com.example.lazyco.core.QuartzScheduler.CronJob.CronJobExecutionLog;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractModel;
import com.example.lazyco.core.QuartzScheduler.CronJob.CronJobSchedule.CronJobSchedule;
import jakarta.persistence.*;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.envers.Audited;

@Audited
@Getter
@Setter
@Entity
@DynamicInsert
@Table(
    name = "cron_job_execution_log",
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

  @ManyToOne
  @JoinColumn(name = "cron_job_schedule_id")
  private CronJobSchedule cronJobSchedule;

  @Column(name = "start_time")
  private Date startTime;

  @Column(name = "end_time")
  private Date endTime;

  @Enumerated(EnumType.STRING)
  private CronJobStatusEnum status;

  private String remarks;
}

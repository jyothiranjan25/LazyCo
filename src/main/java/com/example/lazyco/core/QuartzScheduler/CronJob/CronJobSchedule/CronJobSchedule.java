package com.example.lazyco.core.QuartzScheduler.CronJob.CronJobSchedule;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractModel;
import com.example.lazyco.core.QuartzScheduler.CronJob.CronJobExecutionLog.CronJobExecutionLog;
import com.example.lazyco.core.QuartzScheduler.CronJob.CronJobTypeEnum;
import jakarta.persistence.*;
import java.util.List;
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
    name = "cron_job_schedule",
    comment = "Table storing cron job schedules",
    indexes = {
      @Index(name = "idx_cron_job_schedule_cron_job_type", columnList = "cron_job_type"),
      @Index(name = "idx_cron_job_schedule_status", columnList = "status")
    })
@EntityListeners(CronJobScheduleListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CronJobSchedule extends AbstractModel {

  @Column(name = "cron_job_type")
  @Enumerated(EnumType.STRING)
  private CronJobTypeEnum cronJobType;

  @Column(name = "description")
  private String description;

  @Column(name = "cron_expression")
  private String cronExpression;

  @Column(name = "failure_limit")
  private Integer failureLimit;

  @Column(name = "status", columnDefinition = "boolean default true")
  private Boolean status;

  @OneToMany(mappedBy = "cronJobSchedule", orphanRemoval = true)
  private List<CronJobExecutionLog> cronJobExecutionLogs;
}

package com.example.lazyco.core.QuartzScheduler.CronJob.CronJobExecutionLog;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.FieldPath;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.QuartzScheduler.CronJob.CronJobTypeEnum;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = CronJobExecutionLog.class)
public class CronJobExecutionLogDTO extends AbstractDTO<CronJobExecutionLogDTO> {

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "cronJobSchedule.id")
  private Long cronJobScheduleId;

  @InternalFilterableField private Date startTime;

  @InternalFilterableField private Date endTime;

  @InternalFilterableField private CronJobStatusEnum status;

  private String remarks;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "cronJobSchedule.cronJobType")
  private CronJobTypeEnum cronJobType;

  private String description;

  private Integer failureLimit;
}

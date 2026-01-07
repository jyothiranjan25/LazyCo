package com.example.lazyco.core.QuartzScheduler.CronJob.CronJobSchedule;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.QuartzScheduler.CronJob.CronJobExecutionLog.CronJobExecutionLogDTO;
import com.example.lazyco.core.QuartzScheduler.CronJob.CronJobTypeEnum;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = CronJobSchedule.class)
public class CronJobScheduleDTO extends AbstractDTO<CronJobScheduleDTO> {

  @InternalFilterableField private CronJobTypeEnum cronJobType;
  private Long appUserId;
  private String appUserEmail;
  private String appUserName;
  private String description;
  @InternalFilterableField private String cronExpression;
  private Integer failureLimit;
  @InternalFilterableField private Boolean status;
  private List<CronJobExecutionLogDTO> cronJobExecutionLogs;
  private Boolean cronJobTypes;
}

package com.example.lazyco.backend.core.QuartzScheduler.CronJob.CronJobExecutionLog;

import com.example.lazyco.backend.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CronJobExecutionLogMapper
    extends AbstractMapper<CronJobExecutionLogDTO, CronJobExecutionLog> {

  @Mapping(source = "cronJobSchedule.id", target = "cronJobScheduleId")
  @Mapping(source = "cronJobSchedule.cronJobType", target = "cronJobType")
  @Mapping(source = "cronJobSchedule.description", target = "description")
  @Mapping(source = "cronJobSchedule.failureLimit", target = "failureLimit")
  CronJobExecutionLogDTO map(CronJobExecutionLog entity);
}

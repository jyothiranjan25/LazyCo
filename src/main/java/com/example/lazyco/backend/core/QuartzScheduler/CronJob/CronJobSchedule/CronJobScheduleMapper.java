package com.example.lazyco.backend.core.QuartzScheduler.CronJob.CronJobSchedule;

import com.example.lazyco.backend.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CronJobScheduleMapper
    extends AbstractMapper<CronJobScheduleDTO, CronJobSchedule> {}

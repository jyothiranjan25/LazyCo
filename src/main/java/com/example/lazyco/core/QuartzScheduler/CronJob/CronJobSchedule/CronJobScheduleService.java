package com.example.lazyco.core.QuartzScheduler.CronJob.CronJobSchedule;

import com.example.lazyco.core.AbstractClasses.Service.AbstractService;
import org.springframework.stereotype.Service;

@Service
public class CronJobScheduleService extends AbstractService<CronJobScheduleDTO, CronJobSchedule> {

  public CronJobScheduleService(CronJobScheduleMapper mapper) {
    super(mapper);
  }

  @Override
  protected void makeUpdates(CronJobScheduleDTO source, CronJobSchedule target) {
    if (source.getStatus() != null) target.setStatus(source.getStatus());
    if (source.getCronExpression() != null) target.setCronExpression(source.getCronExpression());
    if (source.getDescription() != null) target.setDescription(source.getDescription());
    if (source.getFailureLimit() != null) target.setFailureLimit(source.getFailureLimit());
  }
}

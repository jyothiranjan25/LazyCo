package com.example.lazyco.backend.core.QuartzScheduler.CronJob;

import lombok.Getter;
import org.quartz.Job;

@Getter
public enum CronJobTypeEnum {
  AUDIT_ENTRY_DELETION(null, "used to delete audit entries older than 2 months"),
  ;
  private final Class<? extends Job> cronJobType;
  private final String description;

  CronJobTypeEnum(Class<? extends Job> cronJobType, String description) {
    this.description = description;
    this.cronJobType = cronJobType;
  }
}

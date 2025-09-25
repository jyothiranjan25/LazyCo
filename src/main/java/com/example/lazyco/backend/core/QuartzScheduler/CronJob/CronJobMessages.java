package com.example.lazyco.backend.core.QuartzScheduler.CronJob;

import com.example.lazyco.backend.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum CronJobMessages implements MessageCodes {
  NOTIFICATION_EMAIL_SUBJECT("CRON_JOB.NOTIFICATION_EMAIL_SUBJECT"),
  INVALID_CRON_EXPRESSION("CRON_JOB.INVALID_CRON_EXPRESSION"),
  ;

  private final String value;

  CronJobMessages(String value) {
    this.value = value;
  }
}

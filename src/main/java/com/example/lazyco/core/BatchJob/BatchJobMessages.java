package com.example.lazyco.core.BatchJob;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum BatchJobMessages implements MessageCodes {
  ALREADY_ACTIVE("BATCH_JOB.ALREADY_ACTIVE"),
  PRE_PROCESS_FAILURE("BATCH_JOB.PRE_PROCESS_FAILURE"),
  BATCH_JOB_NOTIFICATION_MAIL_SUBJECT("BATCH_JOB.BATCH_JOB_NOTIFICATION_MAIL_SUBJECT"),
  PROCESS_FAILURE("BATCH_JOB.PROCESS_FAILURE"),
  NOT_FOUND("BATCH_JOB.NOT_FOUND");

  private final String value;

  BatchJobMessages(String value) {
    this.value = value;
  }
}

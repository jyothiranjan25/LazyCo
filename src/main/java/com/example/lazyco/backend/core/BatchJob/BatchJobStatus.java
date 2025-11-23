package com.example.lazyco.backend.core.BatchJob;

import java.util.HashSet;
import java.util.Set;
import lombok.Getter;

public enum BatchJobStatus {
  INITIALIZED("STARTING"),
  RUNNING("STARTED"),
  COMPLETED("COMPLETED"),
  FAILED("FAILED"),
  PAUSED("STOPPED,STOPPING"),
  RESTARTED("STARTED"),
  TERMINATED("ABANDONED"),
  ;
  @Getter private final String batchJobStatus;

  BatchJobStatus(String batchJobStatus) {
    this.batchJobStatus = batchJobStatus;
  }

  public static Set<BatchJobStatus> getInturruptedStatusSet() {
    Set<BatchJobStatus> set = new HashSet<>();
    set.add(PAUSED);
    set.add(TERMINATED);
    return set;
  }

  public static Set<BatchJobStatus> getCompletedStatusSet() {
    Set<BatchJobStatus> set = new HashSet<>();
    set.add(COMPLETED);
    set.add(FAILED);
    set.add(TERMINATED);
    return set;
  }

  public static Set<BatchJobStatus> getActiveStatusSet() {
    Set<BatchJobStatus> set = new HashSet<>();
    set.add(INITIALIZED);
    set.add(RUNNING);
    set.add(RESTARTED);
    return set;
  }
}

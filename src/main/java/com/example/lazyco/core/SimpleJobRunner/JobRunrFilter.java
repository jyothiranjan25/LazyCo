package com.example.lazyco.core.SimpleJobRunner;

import org.jobrunr.jobs.Job;
import org.jobrunr.jobs.filters.JobServerFilter;
import org.springframework.stereotype.Component;

@Component
public class JobRunrFilter implements JobServerFilter {

  @Override
  public void onProcessing(Job job) {
    System.out.println("Background jobRunr started: Job ID " + job.getId());
  }

  @Override
  public void onProcessingSucceeded(Job job) {
    System.out.println("Background jobRunr executed: Job ID " + job.getId() + " succeeded.");
  }

  @Override
  public void onProcessingFailed(Job job, Exception e) {
    System.out.println("Background jobRunr executed: Job ID " + job.getId() + " failed.");
  }
}

package com.example.lazyco.backend.core.BatchJob.SpringBatch;

import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class AbstractJobListener implements JobExecutionListener {

  @Override
  public void beforeJob(JobExecution jobExecution) {
    ApplicationLogger.info(
        "Starting Spring Batch job execution: " + jobExecution.getJobInstance().getJobName());
  }

  @Override
  public void afterJob(JobExecution jobExecution) {
    ApplicationLogger.info(
        "Spring Batch job completed: " + jobExecution.getJobInstance().getJobName());
  }
}

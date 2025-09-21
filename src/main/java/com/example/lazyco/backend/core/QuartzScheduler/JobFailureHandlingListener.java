package com.example.lazyco.backend.core.QuartzScheduler;

import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import org.quartz.*;
import org.springframework.stereotype.Component;

@Component
public class JobFailureHandlingListener implements JobListener {

  @Override
  public String getName() {
    return "FailJobListener";
  }

  @Override
  public void jobToBeExecuted(JobExecutionContext jobExecutionContext) {
    Class<? extends Job> jobClass = jobExecutionContext.getJobDetail().getJobClass();
    String jobName = jobExecutionContext.getJobDetail().getKey().getName();
    String startTime = jobExecutionContext.getFireTime().toString();
    String name = jobClass.getSimpleName();
    ApplicationLogger.info(
        "Job " + jobName + "[" + name + "]" + " is about to be executed[" + startTime + "]",
        jobClass);
  }

  @Override
  public void jobExecutionVetoed(JobExecutionContext jobExecutionContext) {
    Class<? extends Job> jobClass = jobExecutionContext.getJobDetail().getJobClass();
    String jobName = jobExecutionContext.getJobDetail().getKey().getName();
    String name = jobClass.getSimpleName();
    ApplicationLogger.info("Job " + jobName + "[" + name + "]" + " was vetoed.", jobClass);
  }

  @Override
  public void jobWasExecuted(JobExecutionContext jobExecutionContext, JobExecutionException e) {
    Class<? extends Job> jobClass = jobExecutionContext.getJobDetail().getJobClass();
    String jobName = jobExecutionContext.getJobDetail().getKey().getName();
    String nextFireTime = jobExecutionContext.getNextFireTime().toString();
    String name = jobClass.getSimpleName();
    // Job retries will be put in later.
    if (e == null) {
      ApplicationLogger.info(
          String.format(
              "Job \"%s\" [%s] executed successfully. Next run at [%s].",
              jobName, name, nextFireTime),
          jobClass);
    } else {
      ApplicationLogger.info(
          "Job " + jobName + "[" + name + "]" + " failed with exception: " + e.getMessage(),
          jobClass);
    }
  }
}

package com.example.lazyco.backend.core.BatchJob.SpringBatch;

import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.stereotype.Service;

@Service
public class SpringBatchAction {

  private final JobExplorer jobExplorer;
  private final JobOperator jobOperator;

  public SpringBatchAction(JobExplorer jobExplorer, JobOperator jobOperator) {
    this.jobExplorer = jobExplorer;
    this.jobOperator = jobOperator;
  }

  public Long getRunningJobCount(String jobName) {
    try {
      return jobExplorer.getJobInstanceCount(jobName);
    } catch (Exception e) {
      ApplicationLogger.error("Failed to get running job count", e);
      return 0L;
    }
  }

  public JobExecution getLatestJobExecution(Long jobId) {
    try {
      JobInstance jobInstances = jobExplorer.getJobInstance(jobId);
      if (jobInstances != null) {
        JobExecution executions = jobExplorer.getLastJobExecution(jobInstances);
        if (executions != null) {
          return executions;
        }
      }
    } catch (Exception e) {
      ApplicationLogger.error("Failed to get job execution for: " + jobId, e);
    }
    return null;
  }

  public boolean isJobRunning(Long jobId) {
    JobExecution execution = getLatestJobExecution(jobId);
    return execution != null && execution.getStatus().isRunning();
  }

  public boolean stopJob(Long jobId) {
    JobExecution exec = getLatestJobExecution(jobId);
    if (exec != null && exec.getStatus().isRunning()) {
      try {
        jobOperator.stop(exec.getId());
        ApplicationLogger.info("Stop requested for job: " + jobId);
        return true;
      } catch (Exception e) {
        ApplicationLogger.error("Failed to stop job: " + jobId, e);
      }
    }
    return false;
  }

  public boolean restartJob(Long jobId) {
    JobExecution exec = getLatestJobExecution(jobId);
    if (exec != null
        && (exec.getStatus() == BatchStatus.FAILED || exec.getStatus() == BatchStatus.STOPPED)) {
      try {
        long newExecId = jobOperator.restart(exec.getId());
        ApplicationLogger.info("Restarted job: " + jobId + " new executionId=" + newExecId);
        return true;
      } catch (Exception e) {
        ApplicationLogger.error("Failed to restart job: " + jobId, e);
      }
    }
    return false;
  }

  public boolean terminateJob(Long jobId) {
    JobExecution exec = getLatestJobExecution(jobId);
    if (exec != null) {
      try {
        jobOperator.abandon(exec.getId());
        ApplicationLogger.info("Job abandoned (terminated): " + jobId);
        return true;
      } catch (Exception e) {
        ApplicationLogger.error("Failed to abandon job: " + jobId, e);
      }
    }
    return false;
  }
}

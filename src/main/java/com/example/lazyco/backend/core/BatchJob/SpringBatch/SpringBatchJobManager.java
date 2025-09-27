package com.example.lazyco.backend.core.BatchJob.SpringBatch;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import java.util.List;
import java.util.Set;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.stereotype.Service;

@Service
public class SpringBatchJobManager {

  private final JobExplorer jobExplorer;
  private final JobOperator jobOperator;

  public SpringBatchJobManager(JobExplorer jobExplorer, JobOperator jobOperator) {
    this.jobExplorer = jobExplorer;
    this.jobOperator = jobOperator;
  }

  /** Launch a Spring Batch job in the background */
  public <T, P extends AbstractDTO<?>> void executeJob(
      AbstractSpringBatchJob<T, P> batchJob, List<T> inputData, String jobName) {
    try {
      ApplicationLogger.info(
          "Executing Spring Batch job: " + jobName + " with " + inputData.size() + " items");
      batchJob.executeJob(inputData, jobName);
    } catch (Exception e) {
      ApplicationLogger.error("Failed to execute Spring Batch job: " + jobName, e);
      throw new RuntimeException("Batch job execution failed", e);
    }
  }

  /** Latest execution for a given job name */
  public JobExecution getLatestJobExecution(String jobName) {
    try {
      List<JobInstance> jobInstances = jobExplorer.getJobInstances(jobName, 0, 1);
      if (!jobInstances.isEmpty()) {
        JobInstance latestInstance = jobInstances.get(0);
        List<JobExecution> executions = jobExplorer.getJobExecutions(latestInstance);
        if (!executions.isEmpty()) {
          return executions.get(executions.size() - 1);
        }
      }
    } catch (Exception e) {
      ApplicationLogger.error("Failed to get job execution for: " + jobName, e);
    }
    return null;
  }

  /** Check if a job is running */
  public boolean isJobRunning(String jobName) {
    JobExecution execution = getLatestJobExecution(jobName);
    return execution != null && execution.getStatus().isRunning();
  }

  /** Number of currently running executions for this job */
  public int getRunningJobCount(String jobName) {
    try {
      Set<JobExecution> running = jobExplorer.findRunningJobExecutions(jobName);
      return running.size();
    } catch (Exception e) {
      ApplicationLogger.error("Failed to get running job count", e);
      return 0;
    }
  }

  /** Stop a running job */
  public boolean stopJob(String jobName) {
    JobExecution exec = getLatestJobExecution(jobName);
    if (exec != null && exec.getStatus().isRunning()) {
      try {
        jobOperator.stop(exec.getId());
        ApplicationLogger.info("Stop requested for job: " + jobName);
        return true;
      } catch (Exception e) {
        ApplicationLogger.error("Failed to stop job: " + jobName, e);
      }
    }
    return false;
  }

  /** Restart a failed or stopped job */
  public boolean restartJob(String jobName) {
    JobExecution exec = getLatestJobExecution(jobName);
    if (exec != null
        && (exec.getStatus() == BatchStatus.FAILED || exec.getStatus() == BatchStatus.STOPPED)) {
      try {
        long newExecId = jobOperator.restart(exec.getId());
        ApplicationLogger.info("Restarted job: " + jobName + " new executionId=" + newExecId);
        return true;
      } catch (Exception e) {
        ApplicationLogger.error("Failed to restart job: " + jobName, e);
      }
    }
    return false;
  }

  /** Terminate a job (abandon it) */
  public boolean terminateJob(String jobName) {
    JobExecution exec = getLatestJobExecution(jobName);
    if (exec != null) {
      try {
        jobOperator.abandon(exec.getId());
        ApplicationLogger.info("Job abandoned (terminated): " + jobName);
        return true;
      } catch (Exception e) {
        ApplicationLogger.error("Failed to abandon job: " + jobName, e);
      }
    }
    return false;
  }
}

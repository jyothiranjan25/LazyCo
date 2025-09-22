package com.example.lazyco.backend.core.BatchJob.SpringBatch;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import java.util.List;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpringBatchJobManager {

  @Autowired private JobExplorer jobExplorer;

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

  /**
   * Get job execution status by job name
   *
   * @param jobName Name of the job
   * @return Latest job execution or null if not found
   */
  public JobExecution getLatestJobExecution(String jobName) {
    try {
      List<JobInstance> jobInstances = jobExplorer.getJobInstances(jobName, 0, 1);
      if (!jobInstances.isEmpty()) {
        JobInstance latestInstance = jobInstances.get(0);
        List<JobExecution> executions = jobExplorer.getJobExecutions(latestInstance);
        if (!executions.isEmpty()) {
          return executions.get(executions.size() - 1); // Get latest execution
        }
      }
    } catch (Exception e) {
      ApplicationLogger.error("Failed to get job execution for: " + jobName, e);
    }
    return null;
  }

  /**
   * Check if a job is currently running
   *
   * @param jobName Name of the job
   * @return true if job is running, false otherwise
   */
  public boolean isJobRunning(String jobName) {
    JobExecution execution = getLatestJobExecution(jobName);
    return execution != null && execution.getStatus().isRunning();
  }

  /**
   * Get running job count
   *
   * @return Number of currently running jobs
   */
  public int getRunningJobCount() {
    try {
      return jobExplorer.findRunningJobExecutions(null).size();
    } catch (Exception e) {
      ApplicationLogger.error("Failed to get running job count", e);
      return 0;
    }
  }

  /**
   * Stop a running job (if supported by the implementation)
   *
   * @param jobName Name of the job to stop
   * @return true if stop was initiated, false otherwise
   */
  public boolean stopJob(String jobName) {
    // Note: Job stopping requires JobOperator which would need additional configuration
    // For now, this is a placeholder for future implementation
    ApplicationLogger.error("Job stopping not yet implemented for: " + jobName);
    return false;
  }
}

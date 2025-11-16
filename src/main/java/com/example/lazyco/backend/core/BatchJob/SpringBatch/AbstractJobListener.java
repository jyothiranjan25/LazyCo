package com.example.lazyco.backend.core.BatchJob.SpringBatch;

import com.example.lazyco.backend.core.AbstractAction;
import com.example.lazyco.backend.core.BatchJob.BatchJob;
import com.example.lazyco.backend.core.BatchJob.BatchJobDTO;
import com.example.lazyco.backend.core.BatchJob.BatchJobService;
import com.example.lazyco.backend.core.DateUtils.DateTimeZoneUtils;
import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import com.example.lazyco.backend.core.Utils.CommonConstants;
import com.example.lazyco.backend.entities.User.UserService;
import com.example.lazyco.backend.entities.UserManagement.AppUser.AppUserDTO;
import com.example.lazyco.backend.entities.UserManagement.UserRole.UserRoleDTO;
import com.example.lazyco.backend.entities.UserManagement.UserRole.UserRoleService;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AbstractJobListener implements JobExecutionListener {

  private final AbstractAction abstractAction;
  private final UserService userService;
  private final UserRoleService userRoleService;
  private final BatchJobService batchJobService;

  @Override
  public void beforeJob(JobExecution jobExecution) {
    ApplicationLogger.info(
        "Starting Spring Batch job execution: " + jobExecution.getJobInstance().getJobName());
    // Retrieve logged-in user details from job parameters
    Long userId = jobExecution.getJobParameters().getLong(CommonConstants.LOGGED_USER);
    Long RoleId = jobExecution.getJobParameters().getLong(CommonConstants.LOGGED_USER_ROLE);
    setLoggedInUserContext(userId, RoleId);

    // Update batch job status to IN_PROGRESS
    Long batchJobId = jobExecution.getJobParameters().getLong(CommonConstants.BATCH_JOB_ID);
    BatchJobDTO batchJobDTO = batchJobService.getById(batchJobId);
    batchJobDTO.setJobId(jobExecution.getJobId());
    batchJobDTO.setProcessedCount(0);
    batchJobDTO.setStatus(BatchJob.BatchJobStatus.RUNNING);
    batchJobService.update(batchJobDTO);
  }

  @Override
  public void afterJob(JobExecution jobExecution) {

    // Update batch job status based on job execution outcome
    Long batchJobId = jobExecution.getJobParameters().getLong(CommonConstants.BATCH_JOB_ID);
    BatchJobDTO batchJobDTO = batchJobService.getById(batchJobId);
    if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
      batchJobDTO.setStatus(BatchJob.BatchJobStatus.COMPLETED);
    } else if (jobExecution.getStatus() == BatchStatus.FAILED) {
      batchJobDTO.setStatus(BatchJob.BatchJobStatus.FAILED);
    } else {
      batchJobDTO.setStatus(BatchJob.BatchJobStatus.TERMINATED);
    }
    batchJobDTO.setEndTime(DateTimeZoneUtils.getCurrentDate());

    long processedCount =
        jobExecution.getStepExecutions().stream().mapToLong(StepExecution::getWriteCount).sum();
    batchJobDTO.setProcessedCount(Math.toIntExact(processedCount));

    long failedCount =
        jobExecution.getStepExecutions().stream().mapToLong(StepExecution::getWriteSkipCount).sum();
    batchJobDTO.setFailedCount(Math.toIntExact(failedCount));
    batchJobService.update(batchJobDTO);

    ApplicationLogger.info(
        "Spring Batch job completed: " + jobExecution.getJobInstance().getJobName());
    abstractAction.clearThreadLocals();
  }

  private void setLoggedInUserContext(Long userId, Long roleId) {
    ApplicationLogger.info(
        "Setting logged-in user context for userId: " + userId + " and roleId: " + roleId);
    // Set logged-in user context
    AppUserDTO userDTO = userService.getUserById(userId);
    abstractAction.setLoggedAppUser(userDTO);

    // Set logged-in user role context
    UserRoleDTO roleDTO = userRoleService.getById(roleId);
    abstractAction.setLoggedUserRole(roleDTO);
  }
}

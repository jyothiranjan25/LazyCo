package com.example.lazyco.core.BatchJob.SpringBatch;

import com.example.lazyco.core.AbstractAction;
import com.example.lazyco.core.BatchJob.BatchJobDTO;
import com.example.lazyco.core.BatchJob.BatchJobService;
import com.example.lazyco.core.BatchJob.BatchJobStatus;
import com.example.lazyco.core.BatchJob.NotifyStatus;
import com.example.lazyco.core.CsvTemplate.CsvService;
import com.example.lazyco.core.CsvTemplate.CsvTemplateDTO;
import com.example.lazyco.core.DateUtils.DateTimeZoneUtils;
import com.example.lazyco.core.Exceptions.ExceptionWrapper;
import com.example.lazyco.core.Exceptions.ResolveException;
import com.example.lazyco.core.Logger.ApplicationLogger;
import com.example.lazyco.core.Utils.CommonConstants;
import com.example.lazyco.entities.User.UserService;
import com.example.lazyco.entities.UserManagement.AppUser.AppUserDTO;
import com.example.lazyco.entities.UserManagement.UserRole.UserRoleDTO;
import com.example.lazyco.entities.UserManagement.UserRole.UserRoleService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.listener.*;
import org.springframework.batch.core.step.StepExecution;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class AbstractBatchJobListener<I, O> extends StepListenerSupport<@NonNull I, @NonNull O>
    implements JobExecutionListener {

  private final AbstractAction abstractAction;
  private final UserService userService;
  private final UserRoleService userRoleService;
  private final BatchJobService batchJobService;
  private final ItemException itemException;
  private final CsvService csvService;
  private JobExecution jobExecution;

  public AbstractBatchJobListener(
      AbstractAction abstractAction,
      UserService userService,
      UserRoleService userRoleService,
      BatchJobService batchJobService,
      ItemException itemException,
      CsvService csvService) {
    this.abstractAction = abstractAction;
    this.userService = userService;
    this.userRoleService = userRoleService;
    this.batchJobService = batchJobService;
    this.itemException = itemException;
    this.csvService = csvService;
  }

  // ==================== JOB LISTENER ====================
  @Override
  public void beforeJob(JobExecution jobExecution) {
    try {
      this.jobExecution = jobExecution;
      ApplicationLogger.info(
          "Starting Spring Batch job execution: " + jobExecution.getJobInstance().getJobName());

      // Retrieve logged-in user details from job parameters
      Long userId = jobExecution.getJobParameters().getLong(CommonConstants.LOGGED_USER);
      Long RoleId = jobExecution.getJobParameters().getLong(CommonConstants.LOGGED_USER_ROLE);
      setLoggedInUserContext(userId, RoleId);

      // Update batch job status to IN_PROGRESS
      Long batchJobId = jobExecution.getJobParameters().getLong(CommonConstants.BATCH_JOB_ID);
      BatchJobDTO batchJobDTO = batchJobService.getById(batchJobId);
      batchJobDTO.setJobId(jobExecution.getId());
      batchJobDTO.setProcessedCount(0);
      batchJobDTO.setStatus(BatchJobStatus.RUNNING);
      batchJobService.executeUpdateNewTransactional(batchJobDTO);
    } catch (Exception e) {
      throw new ExceptionWrapper("Error in beforeJob of AbstractBatchJobListener", e);
    }
  }

  // Set logged-in user and role context for the job execution
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

  @Override
  public void afterJob(JobExecution jobExecution) {
    try {
      // Update batch job status based on job execution outcome
      Long batchJobId = jobExecution.getJobParameters().getLong(CommonConstants.BATCH_JOB_ID);
      BatchJobDTO batchJobDTO = batchJobService.getById(batchJobId);
      if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
        batchJobDTO.setStatus(BatchJobStatus.COMPLETED);
      } else if (jobExecution.getStatus() == BatchStatus.FAILED) {
        batchJobDTO.setStatus(BatchJobStatus.FAILED);
      } else {
        batchJobDTO.setStatus(BatchJobStatus.TERMINATED);
      }
      batchJobDTO.setEndTime(DateTimeZoneUtils.getCurrentDate());

      long failedCount =
          jobExecution.getStepExecutions().stream()
              .mapToLong(se -> se.getExecutionContext().getLong("FAILED_COUNT", 0L))
              .sum();
      batchJobDTO.setFailedCount(Math.toIntExact(failedCount));

      long processedCount =
          jobExecution.getStepExecutions().stream()
              .mapToLong(se -> se.getExecutionContext().getLong("PROCESSED_COUNT", 0L))
              .sum();
      batchJobDTO.setProcessedCount(Math.toIntExact(processedCount));

      NotifyStatus notifyStatus;
      if (batchJobDTO.getNotifyOnCompletion() != null && batchJobDTO.getNotifyOnCompletion()) {
        if (batchJobService.sendNotificationToUser(batchJobDTO))
          notifyStatus = NotifyStatus.SENT_SUCCESS;
        else notifyStatus = NotifyStatus.SENT_FAILURE;
      } else {
        notifyStatus = NotifyStatus.NOT_SENT;
      }

      batchJobDTO.setNotifyStatus(notifyStatus);
      batchJobService.executeUpdateNewTransactional(batchJobDTO);
      ApplicationLogger.info(
          "Spring Batch job completed: " + jobExecution.getJobInstance().getJobName());
    } catch (Exception e) {
      throw new ExceptionWrapper("Error in afterJob of AbstractBatchJobListener", e);
    } finally {
      abstractAction.clearThreadLocals();
    }
  }

  // ==================== STEP LISTENER ====================
  @Override
  public void beforeStep(StepExecution stepExecution) {
    ApplicationLogger.info("[Step Listener] Step started: " + stepExecution.getStepName());
  }

  @Override
  public @Nullable ExitStatus afterStep(StepExecution stepExecution) {
    ApplicationLogger.info(
        "[Step Listener] Step ended: "
            + stepExecution.getStepName()
            + " with status: "
            + stepExecution.getStatus());
    return stepExecution.getExitStatus();
  }

  // ==================== CHUNK LISTENER ====================

  /**
   * Transactional scope: beforeChunk and afterChunk are called within the transaction of the chunk.
   * Be cautious about logging or performing actions that might affect transaction integrity.
   */
  @Override
  public void beforeChunk(Chunk chunk) {
    ApplicationLogger.info(
        "[Chunk Listener] Chunk started with " + chunk.getItems().size() + " items.");
  }

  @Override
  public void afterChunk(Chunk chunk) {
    ApplicationLogger.info(
        "[Chunk Listener] Chunk ended with " + chunk.getItems().size() + " items.");
  }

  @Override
  public void onChunkError(@Nullable Exception exception, Chunk chunk) {
    ApplicationLogger.error("[Chunk Listener] Chunk error: " + chunk.getItems().size(), exception);
  }

  // ==================== ITEM Reader Listener ====================
  @Override
  public void beforeRead() {
    ApplicationLogger.info("[Item Reader Listener] Before read item.");
  }

  @Override
  public void afterRead(I item) {
    ApplicationLogger.info("[Item Reader Listener] After read item: " + item);
  }

  @Override
  public void onReadError(@Nullable Exception ex) {
    ApplicationLogger.error("[Item Reader Listener] Read error: ", ex);
  }

  // ==================== ITEM Processor Listener ====================
  @Override
  public void beforeProcess(I item) {
    ApplicationLogger.info("[Item Processor Listener] Before process item: " + item);
  }

  @Override
  public void afterProcess(I item, @Nullable O result) {
    ApplicationLogger.info(
        "[Item Processor Listener] After process item: " + item + ", result: " + result);
  }

  @Override
  public void onProcessError(I item, @Nullable Exception e) {
    ApplicationLogger.error("[Item Processor Listener] Process error for item: " + item, e);
  }

  // ==================== ITEM Writer Listener ====================
  @Override
  public void beforeWrite(Chunk<? extends @NonNull O> items) {
    ApplicationLogger.info(
        "[Item Writer Listener] Before write " + items.getItems().size() + " items.");
  }

  @Override
  public void afterWrite(Chunk<? extends @NonNull O> items) {
    ApplicationLogger.info(
        "[Item Writer Listener] After write " + items.getItems().size() + " items.");
  }

  @Override
  public void onWriteError(@Nullable Exception exception, Chunk<? extends @NonNull O> items) {
    ApplicationLogger.error(
        "[Item Writer Listener] Write error for " + items.getItems().size() + " items.", exception);
  }

  // ==================== Skip Listener ====================
  @Override
  public void onSkipInRead(@Nullable Throwable t) {
    ApplicationLogger.error("[Skip Listener] Skip in read.", t);
  }

  @Override
  public void onSkipInProcess(@Nullable I item, @Nullable Throwable t) {
    ApplicationLogger.error("[Skip Listener] Skip in process for item: " + item, t);
    itemException.add(item, t);
  }

  @Override
  public void onSkipInWrite(@Nullable O item, @Nullable Throwable t) {
    ApplicationLogger.error("[Skip Listener] Skip in write for item: " + item, t);
    itemException.add(item, t);
  }

  private void writeItemToCsv(String path, Object item, Throwable t) {
    try {
      String fullPath = CommonConstants.TOMCAT_HOME + path;

      // ✅ Thread-safe directory creation using java.nio.file
      Path filePath = Paths.get(fullPath);
      Path parentDir = filePath.getParent();

      if (parentDir != null && !Files.exists(parentDir)) {
        try {
          Files.createDirectories(parentDir); // creates only directories, never files
          // If directories already exist, nothing happens (no exception)
        } catch (IOException e) {
          throw new RuntimeException("Cannot create output directory", e);
        }
      }
      String message =
          (t != null) ? ResolveException.resolveExceptionMessage(t) : "Data Saved Successfully";

      CsvTemplateDTO csvTemplateDTO = new CsvTemplateDTO();
      csvTemplateDTO.setCsvClass(item.getClass());
      csvTemplateDTO.setData(List.of(item));
      csvTemplateDTO.setMessage(message);
      csvService.appendSingleRowToCsv(csvTemplateDTO, fullPath);

      ApplicationLogger.info("Successfully wrote item to CSV: " + fullPath);
    } catch (Exception e) {
      ApplicationLogger.error(
          "Failed to write skipped item to CSV for item: " + item.toString(), e);
    }
  }
}

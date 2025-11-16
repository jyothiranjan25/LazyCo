package com.example.lazyco.backend.core.BatchJob.SpringBatch;

import com.example.lazyco.backend.core.AbstractAction;
import com.example.lazyco.backend.core.BatchJob.BatchJob;
import com.example.lazyco.backend.core.BatchJob.BatchJobDTO;
import com.example.lazyco.backend.core.BatchJob.BatchJobService;
import com.example.lazyco.backend.core.CsvTemplate.CsvService;
import com.example.lazyco.backend.core.CsvTemplate.CsvTemplateDTO;
import com.example.lazyco.backend.core.DateUtils.DateTimeZoneUtils;
import com.example.lazyco.backend.core.Exceptions.ExceptionWrapper;
import com.example.lazyco.backend.core.Exceptions.ResolveException;
import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import com.example.lazyco.backend.core.Utils.CommonConstants;
import com.example.lazyco.backend.entities.User.UserService;
import com.example.lazyco.backend.entities.UserManagement.AppUser.AppUserDTO;
import com.example.lazyco.backend.entities.UserManagement.UserRole.UserRoleDTO;
import com.example.lazyco.backend.entities.UserManagement.UserRole.UserRoleService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.springframework.batch.core.*;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.Chunk;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class AbstractBatchJobListener
    implements JobExecutionListener,
        StepExecutionListener,
        ChunkListener,
        ItemWriteListener<Object>,
        SkipListener<Object, Object> {

  private final AbstractAction abstractAction;
  private final UserService userService;
  private final UserRoleService userRoleService;
  private final BatchJobService batchJobService;
  private final CsvService csvService;
  private JobExecution jobExecution;

  public AbstractBatchJobListener(
      AbstractAction abstractAction,
      UserService userService,
      UserRoleService userRoleService,
      BatchJobService batchJobService,
      CsvService csvService) {
    this.abstractAction = abstractAction;
    this.userService = userService;
    this.userRoleService = userRoleService;
    this.batchJobService = batchJobService;
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
      batchJobDTO.setJobId(jobExecution.getJobId());
      batchJobDTO.setProcessedCount(0);
      batchJobDTO.setStatus(BatchJob.BatchJobStatus.RUNNING);
      batchJobService.update(batchJobDTO);
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
          jobExecution.getStepExecutions().stream()
              .mapToLong(StepExecution::getWriteSkipCount)
              .sum();
      batchJobDTO.setFailedCount(Math.toIntExact(failedCount));
      batchJobService.update(batchJobDTO);
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
    ApplicationLogger.info("Starting Step: " + stepExecution.getStepName());
  }

  @Override
  public ExitStatus afterStep(StepExecution stepExecution) {
    ApplicationLogger.info(
        "Completed Step: "
            + stepExecution.getStepName()
            + " with status: "
            + stepExecution.getStatus());
    return stepExecution.getExitStatus();
  }

  // ==================== CHUNK LISTENER ====================

  @Override
  public void beforeChunk(ChunkContext context) {
    ApplicationLogger.info("Starting Chunk in Step: " + context.getStepContext().getStepName());
  }

  @Override
  public void afterChunk(ChunkContext context) {
    ApplicationLogger.info("Finished Chunk in Step: " + context.getStepContext().getStepName());
  }

  @Override
  public void afterChunkError(ChunkContext context) {
    ApplicationLogger.info(
        "Finished Chunk in Step With Error: " + context.getStepContext().getStepName());
  }

  // ==================== ITEM WRITE LISTENER ====================

  @Override
  public void beforeWrite(Chunk<?> items) {
    ApplicationLogger.info("About to write " + items.getItems().size() + " items.");
  }

  @Override
  public void afterWrite(Chunk<?> items) {
    try {
      if (jobExecution != null) {
        String outputFilePath =
            jobExecution.getJobParameters().getString(CommonConstants.BATCH_JOB_FILE_PATH);

        Object item = items.getItems().get(0);

        if (outputFilePath != null) {
          writeSkippedItemToCsv(outputFilePath, item, null);
        } else {
          ApplicationLogger.error(
              "Processed output file path is null, cannot write processed items");
        }
      }
    } catch (Exception e) {
      ApplicationLogger.error("Error in afterWrite: " + e.getMessage(), e);
    }
  }

  @Override
  public void onWriteError(Exception exception, Chunk<?> items) {
    ApplicationLogger.error("Error while writing items", exception);
  }

  // ==================== SKIP LISTENER ====================

  @Override
  public void onSkipInRead(Throwable t) {
    ApplicationLogger.info("Item Skipped during Read due to: " + t.getMessage());
  }

  @Override
  public void onSkipInProcess(Object item, Throwable t) {
    ApplicationLogger.info(
        "Item Skipped during Process. Item: " + item.toString() + " due to: " + t.getMessage());
  }

  @Override
  public void onSkipInWrite(Object item, Throwable t) {
    try {
      if (jobExecution != null) {
        String outputFilePath =
            jobExecution.getJobParameters().getString(CommonConstants.BATCH_JOB_FILE_PATH);
        if (outputFilePath != null) {
          writeSkippedItemToCsv(outputFilePath, item, t);
        } else {
          ApplicationLogger.error("Output file path is null, cannot write skipped item to CSV");
        }
      } else {
        ApplicationLogger.error("JobExecution is null, cannot write skipped item to CSV");
      }
    } catch (Exception e) {
      ApplicationLogger.error("Error in onSkipInWrite: " + e.getMessage(), e);
    }
  }

  private void writeSkippedItemToCsv(String path, Object item, Throwable t) {
    try {
      String fullPath = CommonConstants.TOMCAT_HOME + path;

      // ✅ Thread-safe directory creation using java.nio.file
      Path filePath = Paths.get(fullPath);
      Path parentDir = filePath.getParent();

      if (parentDir != null) {
        try {
          Files.createDirectories(parentDir); // creates only directories, never files
          // If directories already exist, nothing happens (no exception)
          ApplicationLogger.info("Ensured directory exists: " + parentDir);
        } catch (IOException e) {
          throw new RuntimeException("Cannot create output directory", e);
        }
      }
      String message =
          (t != null) ? ResolveException.resolveExceptionMessage(t) : "Data Saved Successfully";

      CsvTemplateDTO csvTemplateDTO = new CsvTemplateDTO();
      csvTemplateDTO.setCsvClass(item.getClass());
      csvTemplateDTO.setData(List.of(item));
      csvTemplateDTO.setErrorMessage(message);
      csvService.appendSingleRowToCsv(csvTemplateDTO, fullPath);

      ApplicationLogger.info("Successfully wrote skipped item to CSV: " + fullPath);
    } catch (Exception e) {
      ApplicationLogger.error(
          "Failed to write skipped item to CSV for item: " + item.toString(), e);
    }
  }
}

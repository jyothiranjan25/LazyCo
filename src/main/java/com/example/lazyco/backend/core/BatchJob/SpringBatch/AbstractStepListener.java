package com.example.lazyco.backend.core.BatchJob.SpringBatch;

import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import org.springframework.batch.core.*;
import org.springframework.stereotype.Component;

@Component
public class AbstractStepListener implements StepExecutionListener {
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
}

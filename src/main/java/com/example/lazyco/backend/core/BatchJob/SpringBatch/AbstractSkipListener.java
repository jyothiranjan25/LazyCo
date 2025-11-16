package com.example.lazyco.backend.core.BatchJob.SpringBatch;

import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import org.springframework.batch.core.*;
import org.springframework.stereotype.Component;

@Component
public class AbstractSkipListener implements SkipListener<Object, Object>, StepExecutionListener {

  private JobExecution jobExecution;

  @Override
  public void beforeStep(StepExecution stepExecution) {
    this.jobExecution = stepExecution.getJobExecution();
  }

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
    ApplicationLogger.info(
        "Item Skipped during Write. Item: "
            + item.toString()
            + " due to: "
            + t.getMessage()
            + ". Job Execution ID: ");
  }
}

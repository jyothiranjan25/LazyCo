package com.example.lazyco.backend.core.BatchJob.SpringBatch;

import com.example.lazyco.backend.core.CsvTemplate.CsvService;
import com.example.lazyco.backend.core.CsvTemplate.CsvTemplateDTO;
import com.example.lazyco.backend.core.Exceptions.ResolveException;
import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import com.example.lazyco.backend.core.Utils.CommonConstants;
import java.io.File;
import java.util.List;
import org.springframework.batch.core.*;
import org.springframework.stereotype.Component;

@Component
public class AbstractSkipListener implements SkipListener<Object, Object>, StepExecutionListener {

  private JobExecution jobExecution;
  private final CsvService csvService;

  public AbstractSkipListener(CsvService csvService) {
    this.csvService = csvService;
  }

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
    String outputFilePath =
        jobExecution.getJobParameters().getString(CommonConstants.BATCH_JOB_FILE_PATH);
    writeSkippedItemToCsv(outputFilePath, item, t);
  }

  private void writeSkippedItemToCsv(String path, Object item, Throwable t) {
    String fullPath = CommonConstants.TOMCAT_HOME + path;

    // create directory if it doesn't exist
    File file = new File(fullPath);
    file.getParentFile().mkdirs();

    CsvTemplateDTO csvTemplateDTO = new CsvTemplateDTO();
    csvTemplateDTO.setCsvClass(item.getClass());
    csvTemplateDTO.setData(List.of(item));
    csvTemplateDTO.setErrorMessage(ResolveException.resolveExceptionMessage(t));
    csvService.appendSingleRowToCsv(csvTemplateDTO, fullPath);
  }
}

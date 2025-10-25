package com.example.lazyco.backend.core.File.CleanupService;

import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import com.example.lazyco.backend.core.Utils.CommonConstrains;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledFileCleanup {

  @Scheduled(cron = "0 0 0 * * *")
  public void tempFile() {
    FileCleanupService fileCleanupService =
        new FileCleanupService(
            CommonConstrains.TOMCAT_TEMP, // specify the directory path
            30 // specify the days threshold
            );
    ApplicationLogger.info("Cleaning old temp files...");
    fileCleanupService.cleanOldFiles();
  }
}

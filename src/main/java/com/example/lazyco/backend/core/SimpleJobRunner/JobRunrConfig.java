package com.example.lazyco.backend.core.SimpleJobRunner;

import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import jakarta.annotation.PreDestroy;
import javax.sql.DataSource;
import org.jobrunr.configuration.JobRunr;
import org.jobrunr.configuration.JobRunrConfiguration;
import org.jobrunr.jobs.filters.RetryFilter;
import org.jobrunr.scheduling.JobScheduler;
import org.jobrunr.server.BackgroundJobServer;
import org.jobrunr.storage.StorageProvider;
import org.jobrunr.storage.sql.postgres.PostgresStorageProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobRunrConfig {

  @Bean
  public StorageProvider storageProvider(DataSource dataSource) {
    return new PostgresStorageProvider(dataSource);
  }

  @Bean
  public JobScheduler jobScheduler(StorageProvider storageProvider, JobRunrFilter jobRunrFilter) {
    JobRunrConfiguration configuration = JobRunr.configure();
    configuration.useStorageProvider(storageProvider);
    configuration.withJobFilter(new RetryFilter(1), jobRunrFilter);
    configuration.useBackgroundJobServer();
    configuration.useDashboard();
    return configuration.initialize().getJobScheduler();
  }

  @PreDestroy
  public void destroy() {
    ApplicationLogger.info("Shutting down JobRunr Background Job Server...");
    BackgroundJobServer backgroundJobServer = JobRunr.getBackgroundJobServer();
    if (backgroundJobServer != null) {
      if (backgroundJobServer.isRunning()) {
        backgroundJobServer.stop();
      }
    }
    JobRunr.destroy();
  }
}

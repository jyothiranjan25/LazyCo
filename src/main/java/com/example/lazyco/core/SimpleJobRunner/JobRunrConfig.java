package com.example.lazyco.core.SimpleJobRunner;

import com.example.lazyco.core.Logger.ApplicationLogger;
import jakarta.annotation.PreDestroy;
import java.time.Duration;
import javax.sql.DataSource;
import org.jobrunr.configuration.JobRunr;
import org.jobrunr.configuration.JobRunrConfiguration;
import org.jobrunr.jobs.filters.RetryFilter;
import org.jobrunr.scheduling.JobScheduler;
import org.jobrunr.server.BackgroundJobServer;
import org.jobrunr.server.BackgroundJobServerConfiguration;
import org.jobrunr.server.JobActivator;
import org.jobrunr.storage.StorageProvider;
import org.jobrunr.storage.sql.postgres.PostgresStorageProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobRunrConfig {

  @Value("${jobrunr.name:LazyCo-JobRunr-Server}")
  private String jobRunName;

  @Value("${jobrunr.pool-interval-in-seconds:10}")
  private int poolIntervalInSeconds;

  @Value("${jobrunr.delete-succeeded-jobs-after-days:2}")
  private long deleteSucceededJobsAfterDays;

  @Value("${jobrunr.delete-deleted-jobs-after-days:1}")
  private long deleteDeletedJobsAfterDays;

  @Bean
  public StorageProvider storageProvider(DataSource dataSource) {
    return new PostgresStorageProvider(dataSource);
  }

  @Bean
  public JobActivator jobActivator(ApplicationContext ctx) {
    return ctx::getBean;
  }

  @Bean
  public JobScheduler jobScheduler(
      StorageProvider storageProvider, JobActivator jobActivator, JobRunrFilter jobRunrFilter) {
    JobRunrConfiguration configuration = JobRunr.configure();
    configuration.useStorageProvider(storageProvider);
    configuration.useJobActivator(jobActivator);
    configuration.withJobFilter(new RetryFilter(1), jobRunrFilter);
    configuration.useBackgroundJobServer(backgroundJobServerConfiguration());
    configuration.useDashboard();
    return configuration.initialize().getJobScheduler();
  }

  private BackgroundJobServerConfiguration backgroundJobServerConfiguration() {
    return BackgroundJobServerConfiguration.usingStandardBackgroundJobServerConfiguration()
        .andName(jobRunName)
        .andPollIntervalInSeconds(poolIntervalInSeconds)
        .andDeleteSucceededJobsAfter(Duration.ofDays(deleteSucceededJobsAfterDays))
        .andPermanentlyDeleteDeletedJobsAfter(Duration.ofDays(deleteDeletedJobsAfterDays));
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

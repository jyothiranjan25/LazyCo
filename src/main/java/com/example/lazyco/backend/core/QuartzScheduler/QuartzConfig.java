package com.example.lazyco.backend.core.QuartzScheduler;

import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import java.util.Properties;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

@Configuration
public class QuartzConfig {

  @Value("${db.url}")
  private String jdbcUrl;

  @Value("${db.username}")
  private String username;

  @Value("${db.password}")
  private String password;

  @Bean
  public JobFactory jobFactory() {
    return new SpringBeanJobFactory(); // Allows Quartz to use Spring-managed beans
  }

  @Bean
  public JobFailureHandlingListener jobFailureListeners() {
    return new JobFailureHandlingListener();
  }

  private Properties quartzProperties() {
    // Quartz properties
    Properties properties = new Properties();
    properties.setProperty("org.quartz.scheduler.instanceName", "QuartzScheduler");
    properties.setProperty("org.quartz.scheduler.instanceId", "AUTO");
    properties.setProperty("org.quartz.scheduler.skipUpdateCheck", "true");
    properties.setProperty("org.quartz.scheduler.makeSchedulerThreadDaemon", "true");

    // Thread Pool Configuration
    properties.setProperty("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
    properties.setProperty("org.quartz.threadPool.threadCount", "10");
    properties.setProperty("org.quartz.threadPool.threadPriority", "5");

    //    // Job Store Configuration (RAM-based)
    properties.setProperty("org.quartz.jobStore.class", "org.quartz.simpl.RAMJobStore");

    // DON'T use JDBC JobStore It is buggy and causes connection leaks and Memory issues

    // JDBC JobStore Configuration with explicit DataSource configuration
    //    properties.setProperty("org.quartz.jobStore.class",
    // "org.quartz.impl.jdbcjobstore.JobStoreTX");
    //    properties.setProperty("org.quartz.jobStore.useProperties", "false");
    //    properties.setProperty(
    //        "org.quartz.jobStore.driverDelegateClass",
    //        "org.quartz.impl.jdbcjobstore.PostgreSQLDelegate");
    //    properties.setProperty("org.quartz.jobStore.tablePrefix", "QRTZ_");
    //    properties.setProperty("org.quartz.jobStore.misfireThreshold", "60000");
    //
    //    // Configure DataSource for Quartz
    //    properties.setProperty("org.quartz.dataSource.quartzDS.driver", "org.postgresql.Driver");
    //    properties.setProperty("org.quartz.dataSource.quartzDS.URL", jdbcUrl);
    //    properties.setProperty("org.quartz.dataSource.quartzDS.user", username);
    //    properties.setProperty("org.quartz.dataSource.quartzDS.password", password);
    //    properties.setProperty("org.quartz.dataSource.quartzDS.maxConnections", "10");
    //    properties.setProperty("org.quartz.dataSource.quartzDS.validationQuery", "SELECT 1");
    //
    //    // Link JobStore to DataSource
    //    properties.setProperty("org.quartz.jobStore.dataSource", "quartzDS");
    //
    //    // Optional clustering (uncomment if you need clustering)
    //    properties.setProperty("org.quartz.jobStore.isClustered", "true");
    //    properties.setProperty("org.quartz.jobStore.clusterCheckinInterval", "20000");

    // Shutdown hook
    //    properties.setProperty(
    //        "org.quartz.plugin.shutdownhook.class",
    // "org.quartz.plugins.management.ShutdownHookPlugin");
    //    properties.setProperty("org.quartz.plugin.shutdownhook.cleanShutdown", "TRUE");
    return properties;
  }

  @Bean
  public SchedulerFactoryBean schedulerFactoryBean() {
    SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();

    // Set Quartz properties
    schedulerFactory.setQuartzProperties(quartzProperties());

    // Configuration options
    schedulerFactory.setOverwriteExistingJobs(true);
    schedulerFactory.setWaitForJobsToCompleteOnShutdown(false);
    //    schedulerFactory.setStartupDelay(10); // Delay startup by 10 seconds

    // Set job factory
    schedulerFactory.setJobFactory(jobFactory());

    // Set global job listeners
    schedulerFactory.setGlobalJobListeners(jobFailureListeners());

    // You can add Jobs here later using schedulerFactory.setJobDetails(...)
    // You can add triggers here later using schedulerFactory.setTriggers(...)

    //    schedulerFactory.setJobDetails(exampleJobDetail());
    //    schedulerFactory.setTriggers(exampleJobTrigger());

    return schedulerFactory;
  }

  //  @Bean
  //  public JobDetail exampleJobDetail() {
  //    return JobBuilder.newJob(MyJob.class)
  //        .withIdentity("exampleJob", "group1")
  //        .storeDurably()
  //        .build();
  //  }
  //
  //  @Bean
  //  public Trigger exampleJobTrigger() {
  //    return TriggerBuilder.newTrigger()
  //        .forJob(exampleJobDetail())
  //        .withIdentity("exampleJobTrigger", "group1")
  //        .withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * * * ?"))
  //        .build();
  //  }

  // Shutdown default QuartzScheduler after Spring context is initialized
  @EventListener(ContextRefreshedEvent.class)
  public void shutdownDefaultScheduler() {
    try {
      Scheduler defaultScheduler = StdSchedulerFactory.getDefaultScheduler();
      if (defaultScheduler != null
          && "DefaultQuartzScheduler".equals(defaultScheduler.getSchedulerName())) {
        if (!defaultScheduler.isShutdown()) {
          ApplicationLogger.info("Shutdown unwanted DefaultQuartzScheduler.");
          defaultScheduler.shutdown(true);
        }

        // Check if the scheduler is shut down
        if (defaultScheduler.isShutdown()) {
          ApplicationLogger.info("DefaultQuartzScheduler shut down successfully.");
        } else {
          ApplicationLogger.warn("DefaultQuartzScheduler failed to shut down.");
        }
      }
    } catch (SchedulerException e) {
      // Log and ignore
      ApplicationLogger.error("Error shutting down DefaultQuartzScheduler: " + e.getMessage());
    }
  }
}

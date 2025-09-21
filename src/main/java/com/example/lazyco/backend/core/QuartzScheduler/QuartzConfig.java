package com.example.lazyco.backend.core.QuartzScheduler;

import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import jakarta.annotation.PostConstruct;
import java.util.Properties;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.spi.JobFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

@Configuration
public class QuartzConfig {

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
    properties.setProperty("org.quartz.scheduler.skipUpdateCheck", "true");
    properties.setProperty("org.quartz.scheduler.instanceName", "QuartzScheduler");
    properties.setProperty("org.quartz.scheduler.instanceId", "AUTO");
    properties.setProperty("org.quartz.scheduler.makeSchedulerThreadDaemon", "true");

    // Thread Pool Configuration
    properties.setProperty("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
    properties.setProperty("org.quartz.threadPool.threadCount", "10");
    properties.setProperty("org.quartz.threadPool.threadPriority", "5");

    // Job Store Configuration (RAM-based)
    properties.setProperty("org.quartz.jobStore.class", "org.quartz.simpl.RAMJobStore");

    // Shutdown hook
    properties.setProperty(
        "org.quartz.plugin.shutdownhook.class", "org.quartz.plugins.management.ShutdownHookPlugin");
    properties.setProperty("org.quartz.plugin.shutdownhook.cleanShutdown", "TRUE");
    return properties;
  }

  @Bean
  public SchedulerFactoryBean schedulerFactoryBean() {
    SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();

    schedulerFactory.setQuartzProperties(quartzProperties());
    schedulerFactory.setOverwriteExistingJobs(true);
    schedulerFactory.setWaitForJobsToCompleteOnShutdown(false);
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
  //    return JobBuilder.newJob(Job.class)
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

  @PostConstruct
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

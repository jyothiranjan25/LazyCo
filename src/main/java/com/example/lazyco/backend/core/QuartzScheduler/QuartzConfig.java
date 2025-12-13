package com.example.lazyco.backend.core.QuartzScheduler;

import java.util.Properties;
import javax.sql.DataSource;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

@Configuration
public class QuartzConfig {

  @Value("${spring.quartz.scheduler.instanceName:QuartzScheduler}")
  private String instanceName;

  @Value("${spring.quartz.threadPool.threadCount:10}")
  private String threadCount;

  @Value("${spring.quartz.threadPool.threadPriority:5}")
  private String threadPriority;

  @Bean
  public JobFactory jobFactory() {
    return new SpringBeanJobFactory();
  }

  @Bean
  public SchedulerFactoryBean schedulerFactoryBean(
      DataSource dataSource,
      JobFactory jobFactory,
      JobFailureHandlingListener jobFailureListeners) {
    SchedulerFactoryBean factory = new SchedulerFactoryBean();

    factory.setDataSource(dataSource);
    factory.setJobFactory(jobFactory);
    factory.setQuartzProperties(quartzProperties());
    factory.setGlobalJobListeners(jobFailureListeners);

    factory.setOverwriteExistingJobs(true);
    factory.setWaitForJobsToCompleteOnShutdown(true);
    factory.setAutoStartup(true);

    return factory;
  }

  public Properties quartzProperties() {
    Properties props = new Properties();

    props.setProperty("org.quartz.scheduler.instanceName", instanceName);
    props.setProperty("org.quartz.scheduler.instanceId", "AUTO");

    props.setProperty("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
    props.setProperty("org.quartz.threadPool.threadCount", threadCount);
    props.setProperty("org.quartz.threadPool.threadPriority", threadPriority);

    return props;
  }
}

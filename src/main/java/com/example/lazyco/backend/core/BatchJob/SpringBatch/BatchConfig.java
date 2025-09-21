package com.example.lazyco.backend.core.BatchJob.SpringBatch;

import javax.sql.DataSource;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.orm.hibernate5.HibernateTransactionManager;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

  @Bean
  public JobRepository jobRepository(DataSource dataSource, HibernateTransactionManager txManager)
      throws Exception {
    JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
    factory.setDataSource(dataSource);
    factory.setTransactionManager(txManager);
    factory.setDatabaseType("POSTGRES");
    factory.setTablePrefix("BATCH_");
    factory.afterPropertiesSet();
    return factory.getObject();
  }

  @Bean
  public JobLauncher jobLauncher(JobRepository repo, AsyncTaskExecutor taskExecutor)
      throws Exception {
    TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
    jobLauncher.setJobRepository(repo);
    jobLauncher.setTaskExecutor(taskExecutor);
    jobLauncher.afterPropertiesSet();
    return jobLauncher;
  }

  @Bean
  public JobExplorer jobExplorer(DataSource dataSource, HibernateTransactionManager txManager)
      throws Exception {
    JobExplorerFactoryBean factory = new JobExplorerFactoryBean();
    factory.setDataSource(dataSource);
    factory.setTransactionManager(txManager);
    factory.setTablePrefix("BATCH_");
    factory.afterPropertiesSet();
    return factory.getObject();
  }

  @Bean
  public JobRegistry jobRegistry() {
    return new MapJobRegistry();
  }

  @Bean
  public JobOperator jobOperator(
      JobLauncher jobLauncher,
      JobRepository jobRepository,
      JobExplorer jobExplorer,
      JobRegistry jobRegistry)
      throws Exception {
    SimpleJobOperator jobOperator = new SimpleJobOperator();
    jobOperator.setJobLauncher(jobLauncher);
    jobOperator.setJobRepository(jobRepository);
    jobOperator.setJobExplorer(jobExplorer);
    jobOperator.setJobRegistry(jobRegistry);
    jobOperator.afterPropertiesSet();
    return jobOperator;
  }
}

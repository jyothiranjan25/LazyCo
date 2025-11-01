package com.example.lazyco.backend.core.BatchJob.SpringBatch;

import javax.sql.DataSource;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.JobOperatorFactoryBean;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

  private final DataSource dataSource;

  public BatchConfig(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Bean
  @Primary
  public JobRepository jobRepository() throws Exception {
    JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
    factory.setDataSource(dataSource);
    factory.setTransactionManager(new DataSourceTransactionManager(dataSource));
    factory.setTablePrefix("BATCH_");
    factory.afterPropertiesSet();
    return factory.getObject();
  }

  @Bean
  @Primary
  public JobLauncher jobLauncher(JobRepository repo) throws Exception {
    TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
    jobLauncher.setJobRepository(repo);
    SimpleAsyncTaskExecutor simpleAsyncTaskExecutor = new SimpleAsyncTaskExecutor();
    simpleAsyncTaskExecutor.setConcurrencyLimit(5);
    jobLauncher.setTaskExecutor(simpleAsyncTaskExecutor);
    jobLauncher.afterPropertiesSet();
    return jobLauncher;
  }

  @Bean
  @Primary
  public JobExplorer jobExplorer() throws Exception {
    JobExplorerFactoryBean factory = new JobExplorerFactoryBean();
    factory.setDataSource(dataSource);
    factory.setTransactionManager(new DataSourceTransactionManager(dataSource));
    factory.afterPropertiesSet();
    return factory.getObject();
  }

  @Bean
  @Primary
  public JobRegistry jobRegistry() {
    return new MapJobRegistry();
  }

  @Bean
  @Primary
  public JobOperator jobOperator(
      JobLauncher jobLauncher,
      JobRepository jobRepository,
      JobExplorer jobExplorer,
      JobRegistry jobRegistry)
      throws Exception {
    JobOperatorFactoryBean factory = new JobOperatorFactoryBean();
    factory.setJobLauncher(jobLauncher);
    factory.setJobRepository(jobRepository);
    factory.setJobExplorer(jobExplorer);
    factory.setJobRegistry(jobRegistry);
    return factory.getObject();
  }
}

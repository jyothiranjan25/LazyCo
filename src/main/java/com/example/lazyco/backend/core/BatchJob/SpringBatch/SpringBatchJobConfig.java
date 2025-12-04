package com.example.lazyco.backend.core.BatchJob.SpringBatch;

import javax.sql.DataSource;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.JobOperatorFactoryBean;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JdbcJobRepositoryFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@Configuration
public class SpringBatchJobConfig {

  private final DataSource dataSource;

  public SpringBatchJobConfig(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Bean
  public JobRepository jobRepository() throws Exception {
    JdbcJobRepositoryFactoryBean factory = new JdbcJobRepositoryFactoryBean();
    factory.setDataSource(dataSource);
    factory.setTransactionManager(new DataSourceTransactionManager(dataSource));
    factory.setTablePrefix("BATCH_");
    factory.afterPropertiesSet();
    return factory.getObject();
  }

  @Bean
  public JobOperator jobOperator(JobRepository jobRepository, JobRegistry jobRegistry)
      throws Exception {
    JobOperatorFactoryBean factory = new JobOperatorFactoryBean();
    factory.setJobRepository(jobRepository);
    factory.setJobRegistry(jobRegistry);
    factory.setTransactionManager(new DataSourceTransactionManager(dataSource));
    factory.setTaskExecutor(new SimpleAsyncTaskExecutor());
    factory.afterPropertiesSet();
    return factory.getObject();
  }

  @Bean
  public JobRegistry jobRegistry() {
    return new MapJobRegistry();
  }
}

package com.example.lazyco.backend.core.QuartzScheduler;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

@Configuration
public class QuartzSchemaInitializer {

  @Value("${hibernate.hbm2ddl.auto:validate}")
  private String hbm2ddlAuto;

  @Bean
  public DataSourceInitializer batchSchemaInitializer(DataSource dataSource) {
    ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
    if ("create-drop".equalsIgnoreCase(hbm2ddlAuto)) {
      populator.addScript(
          new ClassPathResource("org/quartz/impl/jdbcjobstore/tables_postgres.sql"));
    } else {
      return null;
    }
    populator.setContinueOnError(true); // <- continue even if CREATE TABLE fails
    DataSourceInitializer initializer = new DataSourceInitializer();
    initializer.setDataSource(dataSource);
    initializer.setDatabasePopulator(populator);
    return initializer;
  }
}

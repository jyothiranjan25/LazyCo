package com.example.lazyco.core.QuartzScheduler;

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
    String ddl = hbm2ddlAuto == null ? "" : hbm2ddlAuto.trim().toLowerCase();

    ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
    switch (ddl) {
      case "create-drop":
        populator.addScript(
            new ClassPathResource("org/quartz/impl/jdbcjobstore/tables_postgres.sql"));
        break;
      case "create":
      case "update":
        populator.addScript(
            new ClassPathResource("com/example/lazyco/core/QuartzScheduler/schema_postgres.sql"));
        break;
      default:
        // No action needed for 'validate' or 'none'
        break;
    }
    populator.setContinueOnError(true); // <- continue even if CREATE TABLE fails
    DataSourceInitializer initializer = new DataSourceInitializer();
    initializer.setDataSource(dataSource);
    initializer.setDatabasePopulator(populator);
    return initializer;
  }
}

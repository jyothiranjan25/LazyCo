package com.example.lazyco.core.DatabaseConf;

import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class FlywayConfig {

  @Bean(initMethod = "migrate")
  @DependsOn("dataSource")
  public Flyway flyway(DataSource dataSource) {
    return Flyway.configure()
        .dataSource(dataSource)
        .locations("classpath:migration")
        .baselineOnMigrate(true)
        .validateOnMigrate(true)
        .outOfOrder(false)
        .table("flyway_schema_history")
        .load();
  }
}

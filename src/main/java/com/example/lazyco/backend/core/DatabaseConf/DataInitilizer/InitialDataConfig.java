package com.example.lazyco.backend.core.DatabaseConf.DataInitilizer;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

@Configuration
public class InitialDataConfig {

  private final DataSource dataSource;
  private final ResourceLoader resourceLoader;

  @Value("${application.datasource-initializer.enabled:false}")
  private boolean initializerEnabled;

  @Value("${application.datasource-initializer.mode:default}")
  private String initializerMode;

  public InitialDataConfig(DataSource dataSource, ResourceLoader resourceLoader) {
    this.dataSource = dataSource;
    this.resourceLoader = resourceLoader;
  }

  @Bean
  public DataSourceInitializer dataSourceInitializer() {
    if (!initializerEnabled) {
      return null;
    }

    String path = "";
    Resource scriptResource = resourceLoader.getResource(path);
    if (!scriptResource.exists()) {
      return null; // Avoid configuring if script doesn't exist
    }

    ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
    databasePopulator.addScript(scriptResource);

    DataSourceInitializer initializer = new DataSourceInitializer();
    initializer.setDataSource(dataSource);
    initializer.setDatabasePopulator(databasePopulator);
    initializer.setEnabled(true);

    return initializer;
  }
}

package com.example.lazyco.backend.core.DatabaseConf;

import static com.example.lazyco.backend.core.Utils.CommonConstrains.BACKEND_PACKAGE;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = BACKEND_PACKAGE, considerNestedRepositories = true)
public class MongoConfig {

  @Value("${mongodb.username:}")
  private String username;

  @Value("${mongodb.password:}")
  private String password;

  @Value("${mongodb.authSource:}")
  private String authSource;

  @Value("${mongodb.host:}")
  private String host;

  @Value("${mongodb.port:}")
  private int port;

  @Value("${mongodb.database:}")
  private String mongoDatabase;

  @Bean
  public MongoClient mongoClient() {
    String uri;
    if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
      // Authenticated connection
      uri =
          String.format("mongodb://%s:%s@%s:%d/%s", username, password, host, port, mongoDatabase);
      if (authSource != null && !authSource.isEmpty()) {
        uri += "?authSource=" + authSource;
      }
    } else {
      // Unauthenticated connection
      uri = String.format("mongodb://%s:%d/%s", host, port, mongoDatabase);
    }
    return MongoClients.create(uri);
  }

  @Bean
  public MongoDatabaseFactory mongoDatabaseFactory(MongoClient mongoClient) {
    return new SimpleMongoClientDatabaseFactory(mongoClient, mongoDatabase);
  }

  @Bean
  public MongoTemplate mongoTemplate(MongoDatabaseFactory mongoDatabaseFactory) {
    return new MongoTemplate(mongoDatabaseFactory);
  }

  @Bean
  public MongoTransactionManager mongoTransactionManager(
      MongoDatabaseFactory mongoDatabaseFactory) {
    return new MongoTransactionManager(mongoDatabaseFactory);
  }
}

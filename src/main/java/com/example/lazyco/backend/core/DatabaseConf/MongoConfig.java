package com.example.lazyco.backend.core.DatabaseConf;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

@Configuration
public class MongoConfig {

  @Value("${mongodb.username:}")
  private String username;

  @Value("${mongodb.password:}")
  private String password;

  @Value("${mongodb.authSource:}")
  private String authSource;

  @Value("${mongodb.host:localhost}")
  private String host;

  @Value("${mongodb.port:27017}")
  private int port;

  @Value("${mongodb.database:test}")
  private String mongoDatabase;

  @Bean
  public MongoClient mongoClient() {
    String uri;
    if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
      // Authenticated connection
      uri =
          String.format("mongodb://%s:%s@%s:%d/%s", username, password, host, port, mongoDatabase);
      if (StringUtils.isNotBlank(authSource)) {
        uri += "/?authSource=" + authSource;
      }
    } else {
      // Unauthenticated connection
      uri = String.format("mongodb://%s:%d/%s", host, port, mongoDatabase);
    }

    ConnectionString connectionString = new ConnectionString(uri);

    MongoClientSettings settings =
        MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .applyToSocketSettings(
                builder ->
                    builder.connectTimeout(3, TimeUnit.SECONDS)) // fail fast if not reachable
            .applyToClusterSettings(
                builder ->
                    builder.serverSelectionTimeout(
                        10, TimeUnit.SECONDS)) // server selection timeout
            .retryWrites(true)
            .retryReads(true)
            .build();

    return MongoClients.create(settings);
  }

  @Bean
  public MongoDatabaseFactory mongoDatabaseFactory(MongoClient mongoClient) {
    return new SimpleMongoClientDatabaseFactory(mongoClient, mongoDatabase);
  }

  @Bean
  public MongoTemplate mongoTemplate(MongoDatabaseFactory mongoDatabaseFactory) {
    return new MongoTemplate(mongoDatabaseFactory);
  }
}

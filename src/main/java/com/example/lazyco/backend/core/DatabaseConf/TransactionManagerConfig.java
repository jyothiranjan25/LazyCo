package com.example.lazyco.backend.core.DatabaseConf;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.orm.hibernate5.HibernateTransactionManager;

@Configuration
public class TransactionManagerConfig {

  // ==============================
  // Transaction settings
  // ==============================

  @Value("${spring.transaction.default-timeout:30}")
  private int defaultTransactionTimeout;

  @Primary
  @Bean(name = "transactionManager")
  public HibernateTransactionManager hibernateTransactionManager(SessionFactory sessionFactory) {
    HibernateTransactionManager transactionManager = new HibernateTransactionManager();
    transactionManager.setSessionFactory(sessionFactory);
    transactionManager.setDefaultTimeout(defaultTransactionTimeout);
    transactionManager.setNestedTransactionAllowed(true); // savepoints
    return transactionManager;
  }

  @Lazy
  @Bean(name = "mongoTransactionManager")
  public MongoTransactionManager mongoTransactionManager(
      MongoDatabaseFactory mongoDatabaseFactory) {
    return new MongoTransactionManager(mongoDatabaseFactory);
  }
}

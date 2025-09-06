package com.example.lazyco.backend.core.DatabaseConf;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class TransactionManagerConfig {

    // ==============================
    // Transaction settings
    // ==============================

    @Value("${spring.transaction.default-timeout:30}")
    private int defaultTransactionTimeout;

    @Bean
    public HibernateTransactionManager hibernateTransactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory);
        transactionManager.setDefaultTimeout(defaultTransactionTimeout);
        transactionManager.setNestedTransactionAllowed(true); // savepoints
        return transactionManager;
    }

    @Bean
    public MongoTransactionManager mongoTransactionManager(MongoDatabaseFactory mongoDatabaseFactory) {
        MongoTransactionManager transactionManager = new MongoTransactionManager();
        transactionManager.setDatabaseFactory(mongoDatabaseFactory);
        transactionManager.setDefaultTimeout(defaultTransactionTimeout);
        transactionManager.setNestedTransactionAllowed(true); // savepoints
        return transactionManager;
    }

    @Bean
    @Primary
    public PlatformTransactionManager transactionManager(HibernateTransactionManager hibernateTransactionManager, MongoTransactionManager mongoTransactionManager) {
        return new ChainedTransactionManager(hibernateTransactionManager, mongoTransactionManager);
    }
}

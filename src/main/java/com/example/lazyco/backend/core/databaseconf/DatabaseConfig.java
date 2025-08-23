package com.example.lazyco.backend.core.databaseconf;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateOperations;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class DatabaseConfig {

    @Value("${db.driverClassName}")
    private String driverClassName;

    @Value("${db.url}")
    private String jdbcUrl;

    @Value("${db.username}")
    private String username;

    @Value("${db.password}")
    private String password;

    @Value("${hikari.maximumPoolSize:50}")
    private int maximumPoolSize;

    @Value("${hikari.minimumIdle:10}")
    private int minimumIdle;

    @Value("${hikari.connectionTimeout:30000}")
    private int connectionTimeout;

    @Value("${hikari.idleTimeout:600000}")
    private int idleTimeout;

    @Value("${hikari.maxLifetime:1800000}")
    private int maximumLifetime;

    @Value("${hikari.leakDetectionThreshold:60000}")
    private int leakDetectionThreshold;

    @Value("${hibernate.dialect}")
    private String hibernateDialect;

    @Value("${hibernate.show_sql}")
    private boolean showSql;

    @Value("${hibernate.hbm2ddl.auto:create-drop}")
    private String hibernateHbm2ddlAuto;

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(driverClassName);
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);

        // Connection pool settings for high concurrency
        config.setMaximumPoolSize(maximumPoolSize);  // Maximum connections in pool
        config.setMinimumIdle(minimumIdle);      // Minimum idle connections
        config.setConnectionTimeout(connectionTimeout); // 30 seconds
        config.setIdleTimeout(idleTimeout);  // 10 minutes
        config.setMaxLifetime(maximumLifetime); // 30 minutes
        config.setLeakDetectionThreshold(leakDetectionThreshold); // 1 minute
        config.setValidationTimeout(5000);

        // Performance optimizations
        config.setConnectionTestQuery("SELECT 1");
        config.setInitializationFailTimeout(1);

        // Additional HikariCP optimizations
        config.addDataSourceProperty("reWriteBatchedInserts", "true");  // enables batched inserts
        config.addDataSourceProperty("stringtype", "unspecified");      // avoids some JDBC type issues

        // Pool name for monitoring
        config.setPoolName("HikariPool-Main");
        return new HikariDataSource(config);
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        em.setJpaProperties(hibernateProperties());

        return em;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());
        return transactionManager;
    }

    private Properties hibernateProperties() {
        Properties properties = new Properties();

        // Database dialect
        properties.put(AvailableSettings.DIALECT, hibernateDialect);

        // Connection handling
        properties.put(AvailableSettings.CONNECTION_PROVIDER,
                "org.hibernate.hikaricp.internal.HikariCPConnectionProvider");

        // Performance settings for high concurrency
        properties.put(AvailableSettings.STATEMENT_BATCH_SIZE, "25");
        properties.put(AvailableSettings.ORDER_INSERTS, "true");
        properties.put(AvailableSettings.ORDER_UPDATES, "true");
        properties.put(AvailableSettings.BATCH_VERSIONED_DATA, "true");

        // Second level cache (optional but recommended for high concurrency)
        // properties.put(AvailableSettings.USE_SECOND_LEVEL_CACHE, "true");
        // properties.put(AvailableSettings.USE_QUERY_CACHE, "true");
        // properties.put(AvailableSettings.CACHE_REGION_FACTORY,
        //         "org.hibernate.cache.ehcache.EhCacheRegionFactory");

        // Statistics and logging (disable in production)
        properties.put(AvailableSettings.GENERATE_STATISTICS, "false");
        properties.put(AvailableSettings.SHOW_SQL, showSql);
        properties.put(AvailableSettings.FORMAT_SQL, "true");

        // Schema management
        properties.put(AvailableSettings.HBM2DDL_AUTO, hibernateHbm2ddlAuto);

        // Enable JPA 2.1 features
        properties.put(AvailableSettings.JPA_TRANSACTION_COMPLIANCE, "true");

        // JPA specific settings
        properties.put("hibernate.physical_naming_strategy",
                "org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl");

        // Connection release mode
        properties.put("hibernate.connection.release_mode", "auto");

        return properties;
    }
}

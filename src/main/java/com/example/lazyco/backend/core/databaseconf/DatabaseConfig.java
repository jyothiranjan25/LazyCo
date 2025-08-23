package com.example.lazyco.backend.core.databaseconf;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
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

    // HikariCP settings optimized for high concurrency
    @Value("${hikari.maximumPoolSize:100}")
    private int maximumPoolSize;

    @Value("${hikari.minimumIdle:20}")
    private int minimumIdle;

    @Value("${hikari.connectionTimeout:60000}")
    private int connectionTimeout;

    @Value("${hikari.idleTimeout:900000}")
    private int idleTimeout;

    @Value("${hikari.maxLifetime:1800000}")
    private int maximumLifetime;

    @Value("${hikari.leakDetectionThreshold:60000}")
    private int leakDetectionThreshold;

    @Value("${hikari.validationTimeout:10000}")
    private int validationTimeout;

    @Value("${hikari.initializationFailTimeout:30000}")
    private int initializationFailTimeout;

    @Value("${hikari.keepaliveTime:300000}")
    private int keepaliveTime;

    @Value("${hibernate.dialect}")
    private String hibernateDialect;

    @Value("${hibernate.show_sql:false}")
    private boolean showSql;

    @Value("${hibernate.hbm2ddl.auto:validate}")
    private String hibernateHbm2ddlAuto;

    @Value("${hibernate.use_second_level_cache:false}")
    private boolean useSecondLevelCache;

    @Value("${hibernate.use_query_cache:false}")
    private boolean useQueryCache;

    @Value("${hibernate.cache_region_factory_class}")
    private String hibernateCacheRegionFactory;

    @Bean
    @Primary
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(driverClassName);
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);

        // Connection pool settings optimized for 300-400 concurrent users
        config.setMaximumPoolSize(maximumPoolSize);
        config.setMinimumIdle(minimumIdle);
        config.setConnectionTimeout(connectionTimeout);
        config.setIdleTimeout(idleTimeout);
        config.setMaxLifetime(maximumLifetime);
        config.setLeakDetectionThreshold(leakDetectionThreshold);
        config.setValidationTimeout(validationTimeout);
        config.setInitializationFailTimeout(initializationFailTimeout);
        config.setKeepaliveTime(keepaliveTime);

        // Performance optimizations for PostgreSQL
        config.setConnectionTestQuery("SELECT 1");

        // PostgreSQL specific optimizations
        config.addDataSourceProperty("reWriteBatchedInserts", "true");
        config.addDataSourceProperty("stringtype", "unspecified");
        config.addDataSourceProperty("prepareThreshold", "1");
        config.addDataSourceProperty("preparedStatementCacheQueries", "256");
        config.addDataSourceProperty("preparedStatementCacheSizeMiB", "5");
        config.addDataSourceProperty("databaseMetadataCacheFields", "65536");
        config.addDataSourceProperty("databaseMetadataCacheFieldsMiB", "5");

        // Connection management
        config.addDataSourceProperty("tcpKeepAlive", "true");
        config.addDataSourceProperty("socketTimeout", "0");

        // Pool name for monitoring
        config.setPoolName("HikariPool-HighConcurrency");

        return new HikariDataSource(config);
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setShowSql(showSql);
        em.setJpaVendorAdapter(vendorAdapter);

        em.setJpaProperties(hibernateProperties());
        em.setPersistenceUnitName("default");

        return em;
    }

    @Bean
    @Primary
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        transactionManager.setDefaultTimeout(30); // 30 seconds timeout
        return transactionManager;
    }

    private Properties hibernateProperties() {
        Properties properties = new Properties();

        // Database dialect
        properties.put(AvailableSettings.DIALECT, hibernateDialect);

        // Connection provider - let Spring manage this
        properties.put(AvailableSettings.CONNECTION_PROVIDER_DISABLES_AUTOCOMMIT, "true");

        // Performance settings for high concurrency
        properties.put(AvailableSettings.STATEMENT_BATCH_SIZE, "50");
        properties.put(AvailableSettings.ORDER_INSERTS, "true");
        properties.put(AvailableSettings.ORDER_UPDATES, "true");
        properties.put(AvailableSettings.BATCH_VERSIONED_DATA, "true");

        // Second level cache - enabled for high concurrency
        properties.put(AvailableSettings.USE_SECOND_LEVEL_CACHE, useSecondLevelCache);
        properties.put(AvailableSettings.USE_QUERY_CACHE, useQueryCache);
        properties.put(AvailableSettings.CACHE_REGION_FACTORY, hibernateCacheRegionFactory);

        // Statistics and logging (optimized for production)
        properties.put(AvailableSettings.GENERATE_STATISTICS, "false");
        properties.put(AvailableSettings.SHOW_SQL, showSql);
        properties.put(AvailableSettings.FORMAT_SQL, "false");

        // Schema management
        properties.put(AvailableSettings.HBM2DDL_AUTO, hibernateHbm2ddlAuto);

        // JPA compliance and transaction management
        properties.put(AvailableSettings.JPA_TRANSACTION_COMPLIANCE, "true");
        properties.put(AvailableSettings.CONNECTION_HANDLING, "delayed_acquisition_and_hold");

        // Naming strategy
        properties.put(AvailableSettings.PHYSICAL_NAMING_STRATEGY,
                "org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl");

        // Connection isolation level
        properties.put(AvailableSettings.ISOLATION, "READ_COMMITTED");

        // Additional performance optimizations
        properties.put(AvailableSettings.USE_MINIMAL_PUTS, "true");
        properties.put(AvailableSettings.USE_STRUCTURED_CACHE, "true");

        return properties;
    }
}

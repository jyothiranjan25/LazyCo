package com.example.lazyco.backend.core.DatabaseConf;

import static com.example.lazyco.backend.core.Utils.CommonConstants.BACKEND_PACKAGE;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.util.Properties;
import javax.sql.DataSource;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.hibernate.LocalSessionFactoryBean;

@Configuration
public class PostgresConfig {

  // ==============================
  // Database connection settings
  // ==============================

  @Value("${db.driverClassName:}")
  private String driverClassName;

  @Value("${db.url:}")
  private String jdbcUrl;

  @Value("${db.username:}")
  private String username;

  @Value("${db.password:}")
  private String password;

  // ==============================
  // HikariCP connection pool settings
  // ==============================

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

  @Value("${hikari.validationTimeout:5000}")
  private int validationTimeout;

  @Value("${hikari.leakDetectionThreshold:60000}")
  private int leakDetectionThreshold;

  @Value("${hikari.initializationFailTimeout:30000}")
  private int initializationFailTimeout;

  @Value("${hikari.keepaliveTime:300000}")
  private int keepaliveTime;

  // ==============================
  // Hibernate settings
  // ==============================

  // Hibernate settings
  @Value("${hibernate.dialect:}")
  private String hibernateDialect;

  @Value("${hibernate.show_sql:false}")
  private boolean showSql;

  @Value("${hibernate.format_sql:false}")
  private boolean formatSql;

  @Value("${hibernate.hbm2ddl.auto:validate}")
  private String hibernateHbm2ddlAuto;

  @Value("${hibernate.jdbc.time_zone:UTC}")
  private String hibernateTimezone;

  @Value("${hibernate.use_second_level_cache:false}")
  private boolean useSecondLevelCache;

  @Value("${hibernate.use_query_cache:false}")
  private boolean useQueryCache;

  @Value("${hibernate.cache_region_factory_class:}")
  private String hibernateCacheRegionFactory;

  @Value("${hibernate.cache_provider_class:}")
  private String hibernateCacheProviderClass;

  // ==============================
  // Hibernate Envers (audit) settings
  // ==============================
  @Value("${hibernate.envers.enabled:false}")
  private boolean hibernateEnversEnabled;

  @Value("${hibernate.envers.store_data_at_delete:false}")
  private boolean hibernateEnversStoreDataAtDelete;

  @Value("${hibernate.envers.revision_on_collection_change:false}")
  private boolean hibernateEnversRevisionOnCollectionChange;

  @Bean
  @Primary
  public DataSource dataSource() {
    HikariConfig config = getHikariConfig();

    // --- PostgreSQL-specific optimizations ---
    // Rewrite multi-row inserts into a single batch insert
    config.addDataSourceProperty("reWriteBatchedInserts", "true");
    // Allow flexible string type handling (fixes type issues)
    config.addDataSourceProperty("stringtype", "unspecified");
    // Use server-side prepared statements after 1 execution
    config.addDataSourceProperty("prepareThreshold", "1");
    // Enable client-side prepared statement caching
    config.addDataSourceProperty("cachePrepStmts", "true");
    // Use server-side prepared statements
    config.addDataSourceProperty("useServerPrepStmts", "true");
    // Track session state locally (reduces network round trips)
    config.addDataSourceProperty("useLocalSessionState", "true");
    // Rewrite batched statements into a single request
    config.addDataSourceProperty("rewriteBatchedStatements", "true");
    // Max number of prepared statements to cache
    config.addDataSourceProperty("preparedStatementCacheQueries", "1024");
    // Memory size allocated for prepared statement cache
    config.addDataSourceProperty("preparedStatementCacheSizeMiB", "10");
    // Cache size for database metadata fields
    config.addDataSourceProperty("databaseMetadataCacheFields", "65536");
    // Memory size allocated for metadata cache
    config.addDataSourceProperty("databaseMetadataCacheFieldsMiB", "5");
    // Enable TCP keep-alive to detect dead connections
    config.addDataSourceProperty("tcpKeepAlive", "true");
    // Disable socket timeout (0 = infinite)
    config.addDataSourceProperty("socketTimeout", "0");
    // Fetch 1000 rows per round trip when streaming large results
    config.addDataSourceProperty("defaultRowFetchSize", "1000");
    return new HikariDataSource(config); // Build and return the fully configured DataSource
  }

  private HikariConfig getHikariConfig() {
    HikariConfig config = new HikariConfig();

    // JDBC driver class (e.g., org.postgresql.Driver)
    config.setDriverClassName(driverClassName);
    // Database connection URL
    config.setJdbcUrl(jdbcUrl);
    // Database username
    config.setUsername(username);
    // Database password
    config.setPassword(password);

    // Max number of active connections in the pool
    config.setMaximumPoolSize(maximumPoolSize);
    // Minimum number of idle connections to keep ready
    config.setMinimumIdle(minimumIdle);
    // How long to wait for a free connection before throwing error (ms)
    config.setConnectionTimeout(connectionTimeout);
    // Time a connection can sit idle before being closed (ms)
    config.setIdleTimeout(idleTimeout);
    // Maximum lifetime of a connection before being retired (ms)
    config.setMaxLifetime(maximumLifetime);
    // Log warning if a connection is not closed within this time (ms)
    config.setLeakDetectionThreshold(leakDetectionThreshold);
    // Max time to wait for a connection validation (ms)
    config.setValidationTimeout(validationTimeout);
    // Fail fast timeout if DB cannot be reached at startup (ms)
    config.setInitializationFailTimeout(initializationFailTimeout);
    // Interval at which keepalive queries are sent (ms)
    config.setKeepaliveTime(keepaliveTime);

    // Disable auto-commit, Hibernate manages transactions
    config.setAutoCommit(true);
    // Custom pool name (useful for monitoring/metrics)
    config.setPoolName("HikariPool-HighConcurrency");
    // Lightweight validation query for checking connections
    config.setConnectionTestQuery("SELECT 1");
    return config;
  }

  @Bean
  @Primary
  public LocalSessionFactoryBean sessionFactory() {
    LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
    sessionFactory.setDataSource(dataSource());
    sessionFactory.setPackagesToScan(BACKEND_PACKAGE);
    sessionFactory.setHibernateProperties(hibernateProperties());
    return sessionFactory;
  }

  private Properties hibernateProperties() {
    Properties properties = new Properties();

    // Dialect for SQL generation (e.g., PostgreSQLDialect, MySQLDialect, etc.)
    properties.put(AvailableSettings.DIALECT, hibernateDialect);
    // Number of statements to batch before execution
    properties.put(AvailableSettings.STATEMENT_BATCH_SIZE, "100");
    // Optimize insert batching by ordering inserts by entity
    properties.put(AvailableSettings.ORDER_INSERTS, "true");
    // Optimize update batching by ordering updates by entity
    properties.put(AvailableSettings.ORDER_UPDATES, "true");
    // Store timestamps consistently in UTC
    properties.put(AvailableSettings.JDBC_TIME_ZONE, hibernateTimezone);

    // Enable savepoint support for nested transactions
    properties.put("hibernate.boot.allow_jdbc_metadata_access", "false");

    // Use Spring-managed session context
    properties.put(
        AvailableSettings.CURRENT_SESSION_CONTEXT_CLASS,
        "org.springframework.orm.jpa.hibernate.SpringSessionContext");
    // Disallow updates outside of a transaction
    properties.put(AvailableSettings.ALLOW_UPDATE_OUTSIDE_TRANSACTION, "false");
    // Set maximum depth for outer joins/fetching associations
    properties.put(AvailableSettings.MAX_FETCH_DEPTH, "3");
    // Allow lazy loading outside of transactions
    properties.put(AvailableSettings.ENABLE_LAZY_LOAD_NO_TRANS, "false");

    // Enable/disable second-level cache
    properties.put(AvailableSettings.USE_SECOND_LEVEL_CACHE, useSecondLevelCache);
    // Enable/disable query cache
    properties.put(AvailableSettings.USE_QUERY_CACHE, useQueryCache);
    // Cache region factory class implementation
    properties.put(AvailableSettings.CACHE_REGION_FACTORY, hibernateCacheRegionFactory);
    // Cache provider class implementation
    properties.put("hibernate.cache.provider_class", hibernateCacheProviderClass);

    // Disable runtime statistics collection (expensive in production)
    properties.put(AvailableSettings.GENERATE_STATISTICS, "false");
    // Log SQL statements if enabled
    properties.put(AvailableSettings.SHOW_SQL, showSql);
    // Don’t pretty-print SQL (faster logging, smaller output)
    properties.put(AvailableSettings.FORMAT_SQL, formatSql);

    // Strategy for schema generation/validation (validate, update, create, create-drop)
    properties.put(AvailableSettings.HBM2DDL_AUTO, hibernateHbm2ddlAuto);

    // Ensure JPA transaction behavior is strictly followed
    properties.put(AvailableSettings.JPA_TRANSACTION_COMPLIANCE, "false");
    // Allow Hibernate to handle transaction coordination with Spring
    properties.put(AvailableSettings.PREFER_USER_TRANSACTION, "false");
    // Delay connection acquisition until needed, keep until transaction end
    properties.put(AvailableSettings.CONNECTION_HANDLING, "delayed_acquisition_and_hold");

    // Convert camelCase entity names → snake_case table/column names
    properties.put(
        AvailableSettings.PHYSICAL_NAMING_STRATEGY,
        "org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy");

    // Transaction isolation level (READ_COMMITTED recommended for PostgreSQL)
    properties.put(AvailableSettings.ISOLATION, "READ_COMMITTED");

    // Avoid redundant cache puts when entity already cached
    properties.put(AvailableSettings.USE_MINIMAL_PUTS, "true");
    // Use structured format for cache entries (better debuggability)
    properties.put(AvailableSettings.USE_STRUCTURED_CACHE, "true");

    // Enable Envers auditing (tracks entity changes)
    properties.put("org.hibernate.envers.Audited", "true");
    // Suffix for audit tables
    properties.put("org.hibernate.envers.audit_table_suffix", "_AUD");
    // Enabling/Disabling Envers integration
    properties.put("hibernate.integration.envers.enabled", hibernateEnversEnabled);
    // Store entity data in audit table at deletion time
    properties.put("org.hibernate.envers.store_data_at_delete", hibernateEnversStoreDataAtDelete);
    // Create new revision if only a collection (e.g., list/Set) changes
    properties.put(
        "org.hibernate.envers.revision_on_collection_change",
        hibernateEnversRevisionOnCollectionChange);

    if (showSql) {
      properties.put(
          AvailableSettings.USE_SQL_COMMENTS, "true"); // Add comments to SQL for easier debugging
      properties.put(AvailableSettings.LOG_SLOW_QUERY, "5000"); // Log queries > 5 seconds
    }

    return properties;
  }
}

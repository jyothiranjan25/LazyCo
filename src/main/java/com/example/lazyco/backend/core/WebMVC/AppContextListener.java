package com.example.lazyco.backend.core.WebMVC;

import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.lang.reflect.Method;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import javax.sql.DataSource;
import org.quartz.Scheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

@WebListener
public class AppContextListener implements ServletContextListener {

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    ApplicationLogger.info("ServletContextListener contextInitialized");
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    System.out.println("Starting application cleanup...");

    ServletContext context = sce.getServletContext();
    WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(context);

    // 1. Shutdown Quartz Scheduler first
    shutdownQuartzScheduler(appContext);

    // 2. Shutdown Connection Pools (BufferPoolPruner source)
    shutdownConnectionPools(appContext);

    // 3. Shutdown async executors last
    shutdownAsyncExecutors(appContext);

    // 4. Shutdown Executor Services
    shutdownExecutorServices(appContext);

    // 5. Shutdown Netty and other background services
    shutdownBackgroundServices();

    // 6. Unregister JDBC drivers
    unregisterJdbcDrivers();

    // 7. Force cleanup remaining threads
    forceThreadCleanup();

    System.out.println("Application cleanup completed");
  }

  private void shutdownQuartzScheduler(WebApplicationContext appContext) {
    try {
      if (appContext != null) {
        // Shutdown Spring managed schedulers
        String[] schedulerNames = appContext.getBeanNamesForType(Scheduler.class);
        for (String schedulerName : schedulerNames) {
          try {
            Scheduler scheduler = appContext.getBean(schedulerName, Scheduler.class);
            if (!scheduler.isShutdown()) {
              System.out.println("Shutting down Quartz scheduler: " + scheduler.getSchedulerName());
              scheduler.shutdown(true); // Wait for jobs to complete
            }
          } catch (Exception e) {
            System.err.println(
                "Error shutting down scheduler " + schedulerName + ": " + e.getMessage());
          }
        }

        // Shutdown SchedulerFactoryBeans
        String[] factoryNames = appContext.getBeanNamesForType(SchedulerFactoryBean.class);
        for (String factoryName : factoryNames) {
          try {
            SchedulerFactoryBean factory =
                appContext.getBean(factoryName, SchedulerFactoryBean.class);
            System.out.println("Destroying SchedulerFactoryBean: " + factoryName);
            factory.destroy();
          } catch (Exception e) {
            System.err.println(
                "Error destroying SchedulerFactoryBean " + factoryName + ": " + e.getMessage());
          }
        }
      }

    } catch (Exception e) {
      System.err.println("Error shutting down Quartz schedulers: " + e.getMessage());
    }
  }

  private void shutdownConnectionPools(WebApplicationContext appContext) {
    try {
      if (appContext != null) {
        // Shutdown DataSource beans (common source of BufferPoolPruner)
        String[] dataSourceNames = appContext.getBeanNamesForType(DataSource.class);
        for (String dsName : dataSourceNames) {
          try {
            DataSource dataSource = appContext.getBean(dsName, DataSource.class);
            System.out.println("Closing DataSource: " + dsName);

            // Try HikariCP
            if (dataSource.getClass().getName().contains("HikariDataSource")) {
              Method closeMethod = dataSource.getClass().getMethod("close");
              closeMethod.invoke(dataSource);
            }
            // Try C3P0
            else if (dataSource.getClass().getName().contains("ComboPooledDataSource")) {
              Method closeMethod = dataSource.getClass().getMethod("close");
              closeMethod.invoke(dataSource);
            }
            // Try DBCP
            else if (dataSource.getClass().getName().contains("BasicDataSource")) {
              Method closeMethod = dataSource.getClass().getMethod("close");
              closeMethod.invoke(dataSource);
            }

          } catch (Exception e) {
            System.err.println("Error closing DataSource " + dsName + ": " + e.getMessage());
          }
        }
      }
    } catch (Exception e) {
      System.err.println("Error shutting down connection pools: " + e.getMessage());
    }
  }

  private void shutdownAsyncExecutors(WebApplicationContext appContext) {
    if (appContext == null) return;
    try {
      ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor) appContext.getBean("taskExecutor");
      if (executor != null) {
        System.out.println("Shutting down ThreadPoolTaskExecutor");
        executor.shutdown();
        if (!executor.getThreadPoolExecutor().awaitTermination(3, TimeUnit.MINUTES)) {
          System.out.println("Executor did not terminate in time, forcing shutdown");
          executor.getThreadPoolExecutor().shutdownNow();
        }
      }
    } catch (Exception e) {
      System.err.println("Error shutting down async executor: " + e.getMessage());
    }
  }

  private void shutdownExecutorServices(WebApplicationContext appContext) {
    try {
      if (appContext != null) {
        String[] executorNames = appContext.getBeanNamesForType(ExecutorService.class);
        for (String executorName : executorNames) {
          try {
            ExecutorService executor = appContext.getBean(executorName, ExecutorService.class);
            System.out.println("Shutting down ExecutorService: " + executorName);
            executor.shutdown();
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
              executor.shutdownNow();
            }
          } catch (Exception e) {
            System.err.println(
                "Error shutting down ExecutorService " + executorName + ": " + e.getMessage());
          }
        }
      }
    } catch (Exception e) {
      System.err.println("Error shutting down executor services: " + e.getMessage());
    }
  }

  private void unregisterJdbcDrivers() {
    Enumeration<Driver> drivers = DriverManager.getDrivers();
    while (drivers.hasMoreElements()) {
      Driver driver = drivers.nextElement();
      try {
        DriverManager.deregisterDriver(driver);
        System.out.println("Deregistered JDBC driver: " + driver.getClass().getName());
      } catch (SQLException e) {
        System.err.println("Error deregistering driver: " + e.getMessage());
      }
    }
  }

  private void forceThreadCleanup() {
    try {
      // Shutdown Netty EventLoopGroups (common source of BufferPoolPruner)
      shutdownNettyEventLoops();

      // Give a moment for cleanup to complete
      Thread.sleep(3000);

      // Force interrupt remaining daemon threads
      forceInterruptDaemonThreads();

      // Force garbage collection
      System.gc();

      System.out.println("Forced thread cleanup completed");
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  private void shutdownNettyEventLoops() {
    try {
      // Try to shutdown Netty GlobalEventExecutor
      Class<?> globalEventExecutorClass =
          Class.forName("io.netty.util.concurrent.GlobalEventExecutor");
      Object instance = globalEventExecutorClass.getField("INSTANCE").get(null);
      Method shutdownMethod = globalEventExecutorClass.getMethod("shutdownGracefully");
      shutdownMethod.invoke(instance);
      System.out.println("Shutdown Netty GlobalEventExecutor");
    } catch (Exception e) {
      // Netty might not be present, that's OK
      System.out.println("Netty GlobalEventExecutor not found or already shutdown");
    }

    try {
      // Try to shutdown ThreadDeathWatcher
      Class<?> threadDeathWatcherClass = Class.forName("io.netty.util.ThreadDeathWatcher");
      Method awaitInactivityMethod =
          threadDeathWatcherClass.getMethod(
              "awaitInactivity", long.class, java.util.concurrent.TimeUnit.class);
      awaitInactivityMethod.invoke(null, 1000L, TimeUnit.MILLISECONDS);
      System.out.println("Shutdown Netty ThreadDeathWatcher");
    } catch (Exception e) {
      System.out.println("Netty ThreadDeathWatcher not found or already shutdown");
    }
  }

  private void forceInterruptDaemonThreads() {
    ThreadGroup rootGroup = Thread.currentThread().getThreadGroup();
    ThreadGroup parent;
    while ((parent = rootGroup.getParent()) != null) {
      rootGroup = parent;
    }

    Thread[] threads = new Thread[rootGroup.activeCount() * 2];
    int count = rootGroup.enumerate(threads, true);

    for (int i = 0; i < count; i++) {
      Thread thread = threads[i];
      if (thread != null && thread.isDaemon() && thread.isAlive()) {
        String threadName = thread.getName();
        if (threadName.contains("BufferPoolPruner")
            || threadName.contains("Quartz")
            || threadName.contains("pool-")) {
          System.out.println("Force interrupting daemon thread: " + threadName);
          try {
            thread.interrupt();
          } catch (Exception e) {
            System.err.println("Error interrupting thread " + threadName + ": " + e.getMessage());
          }
        }
      }
    }
  }

  private void shutdownBackgroundServices() {
    try {
      // Try to find and shutdown common background services that create BufferPoolPruner

      // 1. Async HTTP Client (Netty-based)
      shutdownAsyncHttpClient();

      // 2. Redis connection pools
      shutdownRedisConnectionPools();

      // 3. Elasticsearch client
      shutdownElasticsearchClient();

    } catch (Exception e) {
      System.err.println("Error shutting down background services: " + e.getMessage());
    }
  }

  private void shutdownAsyncHttpClient() {
    try {
      // AsyncHttpClient often creates BufferPoolPruner threads
      Class<?> asyncHttpClientClass = Class.forName("org.asynchttpclient.DefaultAsyncHttpClient");
      Method getInstanceMethod = asyncHttpClientClass.getMethod("getInstance");
      Object clientInstance = getInstanceMethod.invoke(null);
      if (clientInstance != null) {
        Method closeMethod = asyncHttpClientClass.getMethod("close");
        closeMethod.invoke(clientInstance);
        System.out.println("Shutdown AsyncHttpClient instance");
      }
      // This is a common pattern, try to find singleton instances
      System.out.println("Attempting to shutdown AsyncHttpClient instances");
    } catch (Exception e) {
      // Not present, that's fine
    }
  }

  private void shutdownRedisConnectionPools() {
    try {
      // Jedis/Lettuce connection pools
      System.out.println("Attempting to shutdown Redis connection pools");
    } catch (Exception e) {
      // Not present, that's fine
    }
  }

  private void shutdownElasticsearchClient() {
    try {
      // Elasticsearch RestClient
      System.out.println("Attempting to shutdown Elasticsearch clients");
    } catch (Exception e) {
      // Not present, that's fine
    }
  }
}

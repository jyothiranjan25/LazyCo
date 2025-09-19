package com.example.lazyco.backend.core.AsyncExecutorConf;

import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import java.time.Duration;
import java.util.concurrent.*;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AsyncTaskExecutorConfig implements AsyncConfigurer, DisposableBean {

  @Value("${spring.task.execution.pool.core-size:2}")
  private int corePoolSize;

  @Value("${spring.task.execution.pool.max-size:5}")
  private int maxPoolSize;

  @Value("${spring.task.execution.pool.queue-capacity:10}")
  private int queueCapacity;

  @Value("${spring.task.execution.keep-alive-seconds:30}")
  private int keepAliveSeconds;

  @Value("${spring.task.execution.thread-name-prefix:AsyncExecutor-}")
  private String threadNamePrefix;

  @Value("${spring.task.execution.await-termination:true}")
  private boolean waitForTasksToCompleteOnShutdown;

  @Value("${spring.task.execution.shutdown-timeout-in-minutes:3}")
  private int shutdownTimeoutInMinutes;

  @Value("${spring.task.execution.allow-core-thread-timeout:true}")
  private boolean allowCoreThreadTimeOut;

  private ThreadPoolTaskExecutor executor;

  public ThreadPoolTaskExecutor createThreadPool() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(corePoolSize); // No core threads kept alive
    executor.setMaxPoolSize(maxPoolSize); // Can grow up to 10 threads
    executor.setQueueCapacity(queueCapacity); // Queue before creating new threads
    executor.setKeepAliveSeconds(keepAliveSeconds); // Idle threads live for the 30s
    executor.setThreadNamePrefix(threadNamePrefix); // Thread name prefix
    executor.setWaitForTasksToCompleteOnShutdown(
        waitForTasksToCompleteOnShutdown); // Wait for tasks to complete on shutdown

    int shutdownTimeout = (int) Duration.ofMinutes(shutdownTimeoutInMinutes).toSeconds();
    executor.setAwaitTerminationSeconds(shutdownTimeout); // Wait for tasks to complete

    executor.setTaskDecorator(new SecurityContextTaskDecorator());
    executor.setRejectedExecutionHandler(
        new ThreadPoolExecutor.CallerRunsPolicy()); // Handle rejected tasks

    executor.initialize(); // Initialize the executor then add timeout
    // Enable core thread timeout (very important)
    executor.getThreadPoolExecutor().allowCoreThreadTimeOut(allowCoreThreadTimeOut);

    return executor;
  }

  @Bean(name = "taskExecutor")
  public AsyncTaskExecutor taskExecutor() {
    this.executor = createThreadPool();
    return new CustomSecurityContextAsyncTaskExecutor(this.executor);
  }

  @Override
  public Executor getAsyncExecutor() {
    return taskExecutor();
  }

  @Override
  public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
    return (throwable, method, objects) -> {
      ApplicationLogger.error(throwable, throwable.getClass());
    };
  }

  @Override
  public void destroy() {
    if (executor != null) {
      executor.shutdown();
    }
  }

  private static class SecurityContextTaskDecorator implements TaskDecorator {
    @Override
    public Runnable decorate(Runnable runnable) {
      return new AsyncContextRunnableWrapper(runnable);
    }
  }

  private static class CustomSecurityContextAsyncTaskExecutor
      implements org.springframework.core.task.AsyncTaskExecutor {
    private final org.springframework.core.task.AsyncTaskExecutor delegate;

    public CustomSecurityContextAsyncTaskExecutor(
        org.springframework.core.task.AsyncTaskExecutor delegateAsyncTaskExecutor) {
      this.delegate = delegateAsyncTaskExecutor;
    }

    public void execute(Runnable task) {
      delegate.execute(new AsyncContextRunnableWrapper(task));
    }

    public void execute(Runnable task, long startTimeout) {
      delegate.execute(new AsyncContextRunnableWrapper(task), startTimeout);
    }

    public Future<?> submit(Runnable task) {
      return delegate.submit(new AsyncContextRunnableWrapper(task));
    }

    public <T> Future<T> submit(Callable<T> task) {
      return delegate.submit(new AsyncContextCallableWrapper<>(task));
    }

    @Override
    public CompletableFuture<Void> submitCompletable(Runnable task) {
      return delegate.submitCompletable(new AsyncContextRunnableWrapper(task));
    }

    @Override
    public <T> CompletableFuture<T> submitCompletable(Callable<T> task) {
      return delegate.submitCompletable(new AsyncContextCallableWrapper<>(task));
    }
  }
}

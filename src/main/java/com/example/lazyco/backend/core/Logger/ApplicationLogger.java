package com.example.lazyco.backend.core.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.message.ParameterizedMessage;

/**
 * Enterprise-grade ApplicationLogger with modern logging features. Provides structured logging,
 * performance optimization, and comprehensive API.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationLogger {

  // Cache loggers for performance
  private static final Map<LogType, Logger> LOGGER_CACHE = new ConcurrentHashMap<>();

  // Performance-optimized logger retrieval with caching
  private static Logger getLogger(LogType logType) {
    return LOGGER_CACHE.computeIfAbsent(logType, type -> LogManager.getLogger(type.name()));
  }

  // Enhanced generic logging method with null safety and structured logging
  private static void log(
      LogLevel level,
      String message,
      Throwable e,
      Class<?> clazz,
      LogType logType,
      Object... params) {
    if (message == null) return;

    Logger logger = getLogger(logType);
    if (!isLevelEnabled(logger, level)) return;

    String logMessage = formatMessage(message, clazz, params);

    switch (level) {
      case INFO:
        if (e != null) logger.info(logMessage, e);
        else logger.info(logMessage);
        break;
      case DEBUG:
        if (e != null) logger.debug(logMessage, e);
        else logger.debug(logMessage);
        break;
      case WARN:
        if (e != null) logger.warn(logMessage, e);
        else logger.warn(logMessage);
        break;
      case ERROR:
        if (e != null) logger.error(logMessage, e);
        else logger.error(logMessage);
        break;
      case FATAL:
        if (e != null) logger.fatal(logMessage, e);
        else logger.fatal(logMessage);
        break;
      case TRACE:
        if (e != null) logger.trace(logMessage, e);
        else logger.trace(logMessage);
        break;
    }
  }

  // Performance check to avoid unnecessary string formatting
  private static boolean isLevelEnabled(Logger logger, LogLevel level) {
    return switch (level) {
      case TRACE -> logger.isTraceEnabled();
      case DEBUG -> logger.isDebugEnabled();
      case INFO -> logger.isInfoEnabled();
      case WARN -> logger.isWarnEnabled();
      case ERROR -> logger.isErrorEnabled();
      case FATAL -> logger.isFatalEnabled();
      default -> true;
    };
  }

  // Enhanced message formatting with parameterized messages
  private static String formatMessage(String message, Class<?> clazz, Object... params) {
    String formattedMessage =
        (params != null && params.length > 0)
            ? new ParameterizedMessage(message, params).getFormattedMessage()
            : message;

    return (clazz != null)
        ? String.format("[%s] %s", clazz.getSimpleName(), formattedMessage)
        : formattedMessage;
  }

  // ================== ERROR LOGGING METHODS ==================
  public static void error(String message, Object... params) {
    log(LogLevel.ERROR, message, null, null, LogType.ErrorLogger, params);
  }

  public static void error(String message, Class<?> clazz, Object... params) {
    log(LogLevel.ERROR, message, null, clazz, LogType.ErrorLogger, params);
  }

  public static void error(Throwable e) {
    log(LogLevel.ERROR, e.getMessage(), e, null, LogType.ErrorLogger);
  }

  public static void error(Throwable e, Class<?> clazz) {
    log(LogLevel.ERROR, e.getMessage(), e, clazz, LogType.ErrorLogger);
  }

  public static void error(String message, Throwable e) {
    log(LogLevel.ERROR, message, e, null, LogType.ErrorLogger);
  }

  public static void error(String message, Throwable e, Class<?> clazz) {
    log(LogLevel.ERROR, message, e, clazz, LogType.ErrorLogger);
  }

  // Lazy evaluation for expensive operations
  public static void error(Supplier<String> messageSupplier) {
    Logger logger = getLogger(LogType.ErrorLogger);
    if (logger.isErrorEnabled()) {
      log(LogLevel.ERROR, messageSupplier.get(), null, null, LogType.ErrorLogger);
    }
  }

  // ================== WARN LOGGING METHODS ==================
  public static void warn(String message, Object... params) {
    log(LogLevel.WARN, message, null, null, LogType.WarnLogger, params);
  }

  public static void warn(String message, Class<?> clazz, Object... params) {
    log(LogLevel.WARN, message, null, clazz, LogType.WarnLogger, params);
  }

  public static void warn(Throwable e) {
    log(LogLevel.WARN, e.getMessage(), e, null, LogType.WarnLogger);
  }

  public static void warn(Throwable e, Class<?> clazz) {
    log(LogLevel.WARN, e.getMessage(), e, clazz, LogType.WarnLogger);
  }

  public static void warn(String message, Throwable e) {
    log(LogLevel.WARN, message, e, null, LogType.WarnLogger);
  }

  public static void warn(String message, Throwable e, Class<?> clazz) {
    log(LogLevel.WARN, message, e, clazz, LogType.WarnLogger);
  }

  public static void warn(Supplier<String> messageSupplier) {
    Logger logger = getLogger(LogType.WarnLogger);
    if (logger.isWarnEnabled()) {
      log(LogLevel.WARN, messageSupplier.get(), null, null, LogType.WarnLogger);
    }
  }

  // ================== INFO LOGGING METHODS ==================
  public static void info(String message, Object... params) {
    log(LogLevel.INFO, message, null, null, LogType.InfoLogger, params);
  }

  public static void info(String message, Class<?> clazz, Object... params) {
    log(LogLevel.INFO, message, null, clazz, LogType.InfoLogger, params);
  }

  public static void info(Throwable e) {
    log(LogLevel.INFO, e.getMessage(), e, null, LogType.InfoLogger);
  }

  public static void info(Throwable e, Class<?> clazz) {
    log(LogLevel.INFO, e.getMessage(), e, clazz, LogType.InfoLogger);
  }

  public static void info(String message, Throwable e) {
    log(LogLevel.INFO, message, e, null, LogType.InfoLogger);
  }

  public static void info(String message, Throwable e, Class<?> clazz) {
    log(LogLevel.INFO, message, e, clazz, LogType.InfoLogger);
  }

  public static void info(Supplier<String> messageSupplier) {
    Logger logger = getLogger(LogType.InfoLogger);
    if (logger.isInfoEnabled()) {
      log(LogLevel.INFO, messageSupplier.get(), null, null, LogType.InfoLogger);
    }
  }

  // ================== DEBUG LOGGING METHODS ==================
  public static void debug(String message, Object... params) {
    log(LogLevel.DEBUG, message, null, null, LogType.DebugLogger, params);
  }

  public static void debug(String message, Class<?> clazz, Object... params) {
    log(LogLevel.DEBUG, message, null, clazz, LogType.DebugLogger, params);
  }

  public static void debug(Throwable e) {
    log(LogLevel.DEBUG, e.getMessage(), e, null, LogType.DebugLogger);
  }

  public static void debug(Throwable e, Class<?> clazz) {
    log(LogLevel.DEBUG, e.getMessage(), e, clazz, LogType.DebugLogger);
  }

  public static void debug(String message, Throwable e) {
    log(LogLevel.DEBUG, message, e, null, LogType.DebugLogger);
  }

  public static void debug(String message, Throwable e, Class<?> clazz) {
    log(LogLevel.DEBUG, message, e, clazz, LogType.DebugLogger);
  }

  public static void debug(Supplier<String> messageSupplier) {
    Logger logger = getLogger(LogType.DebugLogger);
    if (logger.isDebugEnabled()) {
      log(LogLevel.DEBUG, messageSupplier.get(), null, null, LogType.DebugLogger);
    }
  }

  // ================== TRACE LOGGING METHODS ==================
  public static void trace(String message, Object... params) {
    log(LogLevel.TRACE, message, null, null, LogType.TraceLogger, params);
  }

  public static void trace(String message, Class<?> clazz, Object... params) {
    log(LogLevel.TRACE, message, null, clazz, LogType.TraceLogger, params);
  }

  public static void trace(Throwable e) {
    log(LogLevel.TRACE, e.getMessage(), e, null, LogType.TraceLogger);
  }

  public static void trace(Throwable e, Class<?> clazz) {
    log(LogLevel.TRACE, e.getMessage(), e, clazz, LogType.TraceLogger);
  }

  public static void trace(String message, Throwable e) {
    log(LogLevel.TRACE, message, e, null, LogType.TraceLogger);
  }

  public static void trace(String message, Throwable e, Class<?> clazz) {
    log(LogLevel.TRACE, message, e, clazz, LogType.TraceLogger);
  }

  public static void trace(Supplier<String> messageSupplier) {
    Logger logger = getLogger(LogType.TraceLogger);
    if (logger.isTraceEnabled()) {
      log(LogLevel.TRACE, messageSupplier.get(), null, null, LogType.TraceLogger);
    }
  }

  // ================== FATAL LOGGING METHODS ==================
  public static void fatal(String message, Object... params) {
    log(LogLevel.FATAL, message, null, null, LogType.ErrorLogger, params);
  }

  public static void fatal(String message, Class<?> clazz, Object... params) {
    log(LogLevel.FATAL, message, null, clazz, LogType.ErrorLogger, params);
  }

  public static void fatal(Throwable e) {
    log(LogLevel.FATAL, e.getMessage(), e, null, LogType.ErrorLogger);
  }

  public static void fatal(Throwable e, Class<?> clazz) {
    log(LogLevel.FATAL, e.getMessage(), e, clazz, LogType.ErrorLogger);
  }

  public static void fatal(String message, Throwable e) {
    log(LogLevel.FATAL, message, e, null, LogType.ErrorLogger);
  }

  public static void fatal(String message, Throwable e, Class<?> clazz) {
    log(LogLevel.FATAL, message, e, clazz, LogType.ErrorLogger);
  }

  public static void fatal(Supplier<String> messageSupplier) {
    Logger logger = getLogger(LogType.ErrorLogger);
    if (logger.isFatalEnabled()) {
      log(LogLevel.FATAL, messageSupplier.get(), null, null, LogType.ErrorLogger);
    }
  }

  // ================== REQUEST LOGGING METHODS ==================
  public static void request(String message, Object... params) {
    log(LogLevel.INFO, message, null, null, LogType.RequestLogger, params);
  }

  public static void request(String message, Class<?> clazz, Object... params) {
    log(LogLevel.INFO, message, null, clazz, LogType.RequestLogger, params);
  }

  // ================== STRUCTURED LOGGING WITH MDC ==================
  /** Add contextual information to logs using MDC (Mapped Diagnostic Context) */
  public static void addContext(String key, String value) {
    if (key != null && value != null) {
      ThreadContext.put(key, value);
    }
  }

  /** Remove specific context from MDC */
  public static void removeContext(String key) {
    if (key != null) {
      ThreadContext.remove(key);
    }
  }

  /** Clear all MDC context for current thread */
  public static void clearContext() {
    ThreadContext.clearAll();
  }

  /** Add multiple context values at once */
  public static void addContext(Map<String, String> contextMap) {
    if (contextMap != null && !contextMap.isEmpty()) {
      ThreadContext.putAll(contextMap);
    }
  }

  // ================== FLUENT API FOR ADVANCED LOGGING ==================
  /** Fluent API entry point for advanced logging scenarios */
  public static LogBuilder log() {
    return new LogBuilder();
  }

  /** Fluent logging builder for complex logging scenarios */
  public static class LogBuilder {
    private LogLevel level;
    private LogType logType;
    private String message;
    private Throwable throwable;
    private Class<?> clazz;
    private Object[] params;
    private Map<String, String> context;

    public LogBuilder level(LogLevel level) {
      this.level = level;
      return this;
    }

    public LogBuilder type(LogType logType) {
      this.logType = logType;
      return this;
    }

    public LogBuilder message(String message, Object... params) {
      this.message = message;
      this.params = params;
      return this;
    }

    public LogBuilder exception(Throwable throwable) {
      this.throwable = throwable;
      return this;
    }

    public LogBuilder clazz(Class<?> clazz) {
      this.clazz = clazz;
      return this;
    }

    public LogBuilder context(String key, String value) {
      if (this.context == null) {
        this.context = new ConcurrentHashMap<>();
      }
      this.context.put(key, value);
      return this;
    }

    public LogBuilder context(Map<String, String> context) {
      if (this.context == null) {
        this.context = new ConcurrentHashMap<>();
      }
      this.context.putAll(context);
      return this;
    }

    public void execute() {
      if (level == null || logType == null || message == null) {
        throw new IllegalStateException("Level, LogType, and message are required");
      }

      // Add context if provided
      if (context != null && !context.isEmpty()) {
        addContext(context);
      }

      try {
        log(level, message, throwable, clazz, logType, params);
      } finally {
        // Clean up context if we added it
        if (context != null && !context.isEmpty()) {
          context.keySet().forEach(ThreadContext::remove);
        }
      }
    }
  }

  // ================== PERFORMANCE MONITORING ==================
  /** Log method execution time */
  public static void logExecutionTime(String methodName, long startTime) {
    long executionTime = System.currentTimeMillis() - startTime;
    debug("Method '{}' executed in {} ms", methodName, executionTime);
  }

  /** Log method execution time with threshold */
  public static void logExecutionTime(String methodName, long startTime, long thresholdMs) {
    long executionTime = System.currentTimeMillis() - startTime;
    if (executionTime > thresholdMs) {
      warn("Method '{}' took {} ms (threshold: {} ms)", methodName, executionTime, thresholdMs);
    } else {
      debug("Method '{}' executed in {} ms", methodName, executionTime);
    }
  }

  // ================== ENUMS ==================
  public enum LogType {
    RequestLogger,
    ErrorLogger,
    WarnLogger,
    DebugLogger,
    InfoLogger,
    TraceLogger
  }

  public enum LogLevel {
    INFO,
    DEBUG,
    ERROR,
    WARN,
    FATAL,
    TRACE
  }
}

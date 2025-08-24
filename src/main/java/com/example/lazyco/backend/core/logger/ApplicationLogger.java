package com.example.lazyco.backend.core.logger;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationLogger {
    // Generic method to get logger based on LogType
    private static Logger getLogger(LogType logType) {
        return LogManager.getLogger(logType.name());
    }

    // Generic method to log messages
    private static void log(
            LogLevel level, String message, Throwable e, Class<?> clazz, LogType logType) {
        Logger logger = getLogger(logType);
        String logMessage =
                (clazz != null) ? String.format("[%s] %s", clazz.getName(), message) : message;

        switch (level) {
            case INFO:
                if (e != null) {
                    logger.info(logMessage, e);
                } else {
                    logger.info(logMessage);
                }
                break;
            case DEBUG:
                if (e != null) {
                    logger.debug(logMessage, e);
                } else {
                    logger.debug(logMessage);
                }
                break;
            case WARN:
                if (e != null) {
                    logger.warn(logMessage, e);
                } else {
                    logger.warn(logMessage);
                }
                break;
            case ERROR:
                if (e != null) {
                    logger.error(logMessage, e);
                } else {
                    logger.error(logMessage);
                }
                break;
            case FATAL:
                if (e != null) {
                    logger.fatal(logMessage, e);
                } else {
                    logger.fatal(logMessage);
                }
                break;
            case TRACE:
                if (e != null) {
                    logger.trace(logMessage, e);
                } else {
                    logger.trace(logMessage);
                }
                break;
        }
    }

    // Error logging methods
    public static void error(Throwable e, Class<?> clazz) {
        log(LogLevel.ERROR, e.getMessage(), e, null, LogType.ErrorLogger);
    }

    public static void error(Throwable e) {
        log(LogLevel.ERROR, e.getMessage(), e, null, LogType.ErrorLogger);
    }

    public static void error(String message, Class<?> clazz) {
        log(LogLevel.ERROR, message, null, clazz, LogType.ErrorLogger);
    }

    public static void error(String message) {
        log(LogLevel.ERROR, message, null, null, LogType.ErrorLogger);
    }

    // Warn logging methods
    public static void warn(Throwable e, Class<?> clazz) {
        log(LogLevel.WARN, e.getMessage(), e, null, LogType.WarnLogger);
    }

    public static void warn(Throwable e) {
        log(LogLevel.WARN, e.getMessage(), e, null, LogType.WarnLogger);
    }

    public static void warn(String message, Class<?> clazz) {
        log(LogLevel.WARN, message, null, clazz, LogType.WarnLogger);
    }

    public static void warn(String message) {
        log(LogLevel.WARN, message, null, null, LogType.WarnLogger);
    }

    // Debug logging methods
    public static void debug(Throwable e) {
        log(LogLevel.DEBUG, e.getMessage(), e, null, LogType.DebugLogger);
    }

    public static void debug(String message, Class<?> clazz) {
        log(LogLevel.DEBUG, message, null, clazz, LogType.DebugLogger);
    }

    public static void debug(String message) {
        log(LogLevel.DEBUG, message, null, null, LogType.DebugLogger);
    }

    // Info logging methods
    public static void info(Throwable e, Class<?> clazz) {
        log(LogLevel.INFO, e.getMessage(), e, null, LogType.InfoLogger);
    }

    public static void info(Throwable e) {
        log(LogLevel.INFO, e.getMessage(), e, null, LogType.InfoLogger);
    }

    public static void info(String message, Class<?> clazz) {
        log(LogLevel.INFO, message, null, clazz, LogType.InfoLogger);
    }

    public static void info(String message) {
        log(LogLevel.INFO, message, null, null, LogType.InfoLogger);
    }

    // Request logging method
    public static void request(String message) {
        log(LogLevel.INFO, message, null, null, LogType.RequestLogger);
    }

    // Fatal logging methods
    public static void fatal(Throwable e, Class<?> clazz) {
        log(LogLevel.FATAL, e.getMessage(), e, null, LogType.ErrorLogger);
    }

    public static void fatal(Throwable e) {
        log(LogLevel.FATAL, e.getMessage(), e, null, LogType.ErrorLogger);
    }

    public static void fatal(String message, Class<?> clazz) {
        log(LogLevel.FATAL, message, null, clazz, LogType.ErrorLogger);
    }

    public static void fatal(String message) {
        log(LogLevel.FATAL, message, null, null, LogType.ErrorLogger);
    }

    // Trace logging methods
    public static void trace(Throwable e, Class<?> clazz) {
        log(LogLevel.TRACE, e.getMessage(), e, null, LogType.TraceLogger);
    }

    public static void trace(Throwable e) {
        log(LogLevel.TRACE, e.getMessage(), e, null, LogType.TraceLogger);
    }

    public static void trace(String message, Class<?> clazz) {
        log(LogLevel.TRACE, message, null, clazz, LogType.TraceLogger);
    }

    public static void trace(String message) {
        log(LogLevel.TRACE, message, null, null, LogType.TraceLogger);
    }

    // LogType Enum (unchanged)
    public enum LogType {
        RequestLogger,
        ErrorLogger,
        WarnLogger,
        DebugLogger,
        InfoLogger,
        TraceLogger
    }

    // LogLevel Enum (unchanged)
    private enum LogLevel {
        INFO,
        DEBUG,
        ERROR,
        WARN,
        FATAL,
        TRACE
    }
}
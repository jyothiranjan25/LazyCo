package com.example.lazyco.backend.core.DateUtils;

import com.example.lazyco.backend.core.ConfigurationMaster.SystemSettingsMetaData.SystemSettingsKeys;
import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility class for parsing and formatting dates from various string formats. Handles multiple date
 * formats, timezone conversions, and epoch timestamps.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateParser {

  // Thread-safe cache for SimpleDateFormat instances
  private static final Map<String, ThreadLocal<SimpleDateFormat>> FORMAT_CACHE =
      new ConcurrentHashMap<>();

  /**
   * Get system timezone from system property, environment variable, or default. Supports multiple
   * ways to configure timezone for different deployment environments.
   */
  public static ZoneId getSystemTimezone() {
    String timezoneProperty = System.getProperty(SystemSettingsKeys.SYSTEM_TIMEZONE.getValue());
    return DateTimeProps.getSystemTimezone(timezoneProperty);
  }

  /** Get legacy TimeZone for backwards compatibility. */
  public static TimeZone getSystemTimeZone() {
    return TimeZone.getTimeZone(getSystemTimezone());
  }

  /**
   * Parse date string to Date object with timezone awareness. Preserves timezone information when
   * present, converts to system timezone when needed.
   *
   * @param dateString the date string to parse
   * @return the parsed Date object, or null if parsing fails
   */
  public static Date deserializeDate(String dateString) {
    return deserializeDate(dateString, getSystemTimezone());
  }

  /**
   * Parse date string to Date object with explicit target timezone.
   *
   * @param dateString the date string to parse
   * @param targetZone the target timezone for the result
   * @return the parsed Date object in target timezone, or null if parsing fails
   */
  public static Date deserializeDate(String dateString, ZoneId targetZone) {
    if (dateString == null || dateString.trim().isEmpty()) {
      return null;
    }

    dateString = DateTimeProps.normalizeDate(dateString);

    // Handle numeric timestamps
    if (isNumericTimestamp(dateString)) {
      try {
        return parseTimestamp(dateString);
      } catch (Exception e) {
        ApplicationLogger.debug("Failed to parse numeric timestamp: {}", dateString);
      }
    }

    // Try parsing with detected pattern
    String pattern = DateTimeProps.buildDateTimePattern(dateString);
    Date result = tryParseWithPattern(dateString, pattern, targetZone);
    if (result != null) {
      return result;
    }

    // Fallback to trying all known patterns
    for (String fallbackPattern : DateTimeProps.ALL_PATTERNS) {
      result = tryParseWithPattern(dateString, fallbackPattern, targetZone);
      if (result != null) {
        return result;
      }
    }

    // Final fallback using Java Time API
    return tryParseWithTimeAPI(dateString, targetZone);
  }

  /**
   * Parse time string to Time object.
   *
   * @param timeString the time string to parse
   * @return the parsed Time object, or null if parsing fails
   */
  public static Time deserializeTime(String timeString) {
    if (timeString == null || timeString.trim().isEmpty()) {
      return null;
    }

    timeString = timeString.trim();

    // Handle numeric timestamps
    if (isNumericTimestamp(timeString)) {
      Date date = parseTimestamp(timeString);
      return date != null ? new Time(date.getTime()) : null;
    }

    // Try time-specific patterns first
    String[] timePatterns = {
      DateTimeProps.HH_MM_SS, DateTimeProps.HH_MM_SS_SSS, DateTimeProps.HH_MM
    };

    for (String pattern : timePatterns) {
      try {
        SimpleDateFormat sdf = getThreadSafeDateFormat(pattern);
        Date parsedDate = sdf.parse(timeString);
        return new Time(parsedDate.getTime());
      } catch (ParseException e) {
        // continue to next pattern
      }
    }

    // Try full date-time patterns that might contain time
    for (String pattern : DateTimeProps.STANDARD_YEAR_MONTH_DATE) {
      if (pattern.contains("HH")) {
        try {
          SimpleDateFormat sdf = getThreadSafeDateFormat(pattern);
          Date parsedDate = sdf.parse(timeString);
          return new Time(parsedDate.getTime());
        } catch (ParseException e) {
          // continue to next pattern
        }
      }
    }

    return null;
  }

  /**
   * Parse timestamp from long value (milliseconds since epoch).
   *
   * @param timestampMillis timestamp in milliseconds since epoch
   * @return the Date object representing the timestamp
   */
  public static Date parseTimestamp(long timestampMillis) {
    return new Date(timestampMillis);
  }

  /**
   * Parse timestamp from string (handles both milliseconds and seconds since epoch).
   *
   * @param timestampString the timestamp string to parse
   * @return the parsed Date object, or null if parsing fails
   */
  public static Date parseTimestamp(String timestampString) {
    if (timestampString == null || timestampString.trim().isEmpty()) {
      return null;
    }

    try {
      long timestamp = Long.parseLong(timestampString.trim());

      // Auto-detect if timestamp is in seconds or milliseconds
      // Timestamps in seconds are typically 10 digits (until year 2286)
      // Timestamps in milliseconds are typically 13 digits
      if (Math.abs(timestamp) < 1_000_000_000_000L) { // Less than 10^12 = likely seconds
        timestamp = timestamp * 1000L; // Convert to milliseconds
      }

      return new Date(timestamp);

    } catch (NumberFormatException e) {
      ApplicationLogger.debug("Failed to parse numeric timestamp: {}", timestampString);
      return null;
    }
  }

  /**
   * Format Date to string using specified pattern and timezone.
   *
   * @param date the date to format
   * @param pattern the format pattern
   * @param zoneId the timezone to use
   * @return formatted date string
   */
  public static String formatDate(Date date, String pattern, ZoneId zoneId) {
    if (date == null || pattern == null) {
      return null;
    }

    try {
      SimpleDateFormat sdf = getThreadSafeDateFormat(pattern);
      sdf.setTimeZone(TimeZone.getTimeZone(zoneId != null ? zoneId : getSystemTimezone()));
      return sdf.format(date);
    } catch (Exception e) {
      ApplicationLogger.warn(
          "Error formatting date {} with pattern {} and timezone {}", date, pattern, zoneId);
      return null;
    }
  }

  /**
   * Convert date from one timezone to another.
   *
   * @param date the date to convert
   * @param fromZone source timezone (if null, assumes system timezone)
   * @param toZone target timezone
   * @return converted date
   */
  public static Date convertTimezone(Date date, ZoneId fromZone, ZoneId toZone) {
    if (date == null || toZone == null) {
      return date;
    }

    ZoneId sourceZone = fromZone != null ? fromZone : getSystemTimezone();

    if (sourceZone.equals(toZone)) {
      return date; // No conversion needed
    }

    try {
      Instant instant = date.toInstant();
      ZonedDateTime sourceTime = instant.atZone(sourceZone);
      ZonedDateTime targetTime = sourceTime.withZoneSameInstant(toZone);
      return Date.from(targetTime.toInstant());
    } catch (Exception e) {
      ApplicationLogger.warn("Error converting date {} from {} to {}", date, fromZone, toZone);
      return date; // Return original on error
    }
  }

  // Private helper methods

  /** Get thread-safe SimpleDateFormat instance for given pattern. */
  private static SimpleDateFormat getThreadSafeDateFormat(String pattern) {
    ThreadLocal<SimpleDateFormat> tl =
        FORMAT_CACHE.computeIfAbsent(
            pattern, p -> ThreadLocal.withInitial(() -> new SimpleDateFormat(p, Locale.ROOT)));
    return (SimpleDateFormat) tl.get().clone(); // clone for safe mutation
  }

  /** Try parsing with a specific pattern and timezone. */
  private static Date tryParseWithPattern(String dateString, String pattern, ZoneId targetZone) {
    try {
      SimpleDateFormat sdf = getThreadSafeDateFormat(pattern);

      if (pattern.contains("XXX") || pattern.contains("Z")) {
        // Pattern includes timezone - parse then convert if needed
        Date parsedDate = sdf.parse(dateString);
        return convertToTargetTimezone(parsedDate, dateString, targetZone);
      } else {
        // Pattern doesn't include timezone - assume target timezone
        sdf.setTimeZone(TimeZone.getTimeZone(targetZone));
        return sdf.parse(dateString);
      }
    } catch (ParseException e) {
      return null;
    }
  }

  /** Convert parsed date with timezone info to target timezone. */
  private static Date convertToTargetTimezone(Date date, String originalString, ZoneId targetZone) {
    if (date == null || originalString == null || targetZone == null) {
      return date;
    }

    try {
      // Extract timezone from original string
      ZoneId sourceZone = extractTimezoneFromString(originalString);
      if (sourceZone != null && !sourceZone.equals(targetZone)) {
        return convertTimezone(date, sourceZone, targetZone);
      }
    } catch (Exception e) {
      ApplicationLogger.debug("Could not extract timezone from string: {}", originalString);
    }

    return date;
  }

  /** Extract timezone from date string if present. */
  private static ZoneId extractTimezoneFromString(String dateString) {
    if (dateString == null) {
      return null;
    }

    // Handle Z (UTC)
    if (dateString.endsWith("Z")) {
      return ZoneId.of("UTC");
    }

    // Handle +/-HH:MM or +/-HHMM
    if (dateString.matches(".*[+-]\\d{2}:?\\d{2}$")) {
      String tzPart = dateString.replaceAll(".*([+-]\\d{2}:?\\d{2})$", "$1");
      try {
        // Normalize format to +HH:MM
        if (!tzPart.contains(":")) {
          tzPart = tzPart.substring(0, 3) + ":" + tzPart.substring(3);
        }
        return ZoneId.of(tzPart);
      } catch (Exception e) {
        ApplicationLogger.debug("Could not parse timezone offset: {}", tzPart);
      }
    }

    return null;
  }

  /** Fallback parsing using Java Time API. */
  private static Date tryParseWithTimeAPI(String dateString, ZoneId targetZone) {
    if (dateString == null || dateString.trim().isEmpty()) {
      return null;
    }

    // Try ISO instant first
    try {
      Instant instant = Instant.parse(dateString);
      return Date.from(instant);
    } catch (DateTimeParseException e) {
      // Continue with other formats
    }

    // Try with DateTimeFormatter patterns
    for (String pattern : DateTimeProps.STANDARD_YEAR_MONTH_DATE) {
      try {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        if (pattern.contains("HH")) {
          // Has time component
          if (pattern.contains("XXX") || pattern.contains("Z")) {
            // Has timezone
            ZonedDateTime zdt = ZonedDateTime.parse(dateString, formatter);
            return Date.from(zdt.withZoneSameInstant(targetZone).toInstant());
          } else {
            // No timezone - assume target zone
            LocalDateTime ldt = LocalDateTime.parse(dateString, formatter);
            return Date.from(ldt.atZone(targetZone).toInstant());
          }
        } else {
          // Date only - add start of day in target timezone
          LocalDateTime ldt =
              LocalDateTime.parse(
                  dateString + " 00:00:00", DateTimeFormatter.ofPattern(pattern + " HH:mm:ss"));
          return Date.from(ldt.atZone(targetZone).toInstant());
        }
      } catch (DateTimeParseException e) {
        // Continue to next pattern
      }
    }

    return null;
  }

  /** Check if string represents a numeric timestamp. */
  private static boolean isNumericTimestamp(String input) {
    if (input == null || input.isEmpty()) {
      return false;
    }
    // Allow optional leading minus and ensure 10–13 digits.
    return input.matches("^-?\\d{10,13}$");
  }
}

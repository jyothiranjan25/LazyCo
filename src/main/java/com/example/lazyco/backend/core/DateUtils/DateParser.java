package com.example.lazyco.backend.core.DateUtils;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.TimeZone;

/**
 * Utility class for parsing and formatting dates from various string formats. Handles multiple date
 * formats, timezone conversions, and epoch timestamps.
 */
public class DateParser {

  private static final ZoneId SYSTEM_TIMEZONE = ZoneId.systemDefault();

  /**
   * Deserialize date string to Date object using multiple format attempts. Converts to system
   * timezone and handles epoch/Unix timestamps (milliseconds and seconds).
   *
   * @param dateString the date string to parse
   * @return the parsed Date object, or null if parsing fails
   */
  public static Date deserializeDate(String dateString) {
    if (dateString == null || dateString.trim().isEmpty()) {
      return null;
    }

    dateString = dateString.trim();

    // Check if it's a numeric timestamp (epoch time)
    if (isNumericTimestamp(dateString)) {
      return parseTimestamp(dateString);
    }

    // Try each format in the enum
    for (DateTimeFormatEnum format : DateTimeFormatEnum.values()) {
      try {
        // Skip time-only formats for date parsing
        if (format == DateTimeFormatEnum.HH_mm_ss) {
          continue;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(format.getValue());

        // For formats with timezone info, let it parse naturally
        if (format == DateTimeFormatEnum.yyyy_MM_dd_T_HH_mm_ssXXX) {
          Date parsedDate = sdf.parse(dateString);
          return convertToSystemTimezone(parsedDate, dateString);
        } else {
          // For formats without timezone, assume system timezone
          sdf.setTimeZone(TimeZone.getTimeZone(SYSTEM_TIMEZONE));
          return sdf.parse(dateString);
        }
      } catch (ParseException e) {
        // Continue to next format
      }
    }

    // If all SimpleDateFormat attempts fail, try Java 8 Time API
    return tryParseWithTimeAPI(dateString);
  }

  /**
   * Deserialize time string to Time object.
   *
   * @param timeString the time string to parse
   * @return the parsed Time object, or null if parsing fails
   */
  public static Time deserializeTime(String timeString) {
    if (timeString == null || timeString.trim().isEmpty()) {
      return null;
    }

    timeString = timeString.trim();

    try {
      // Try HH:mm:ss format first
      SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormatEnum.HH_mm_ss.getValue());
      Date parsedDate = sdf.parse(timeString);
      return new Time(parsedDate.getTime());
    } catch (ParseException e) {
      // Try other formats that might contain time
      for (DateTimeFormatEnum format : DateTimeFormatEnum.values()) {
        if (format == DateTimeFormatEnum.yyyy_MM_dd) {
          continue; // Skip date-only format
        }

        try {
          SimpleDateFormat sdf = new SimpleDateFormat(format.getValue());
          Date parsedDate = sdf.parse(timeString);
          return new Time(parsedDate.getTime());
        } catch (ParseException ex) {
          // Continue to next format
        }
      }
    }

    return null;
  }

  /**
   * Get current system timezone.
   *
   * @return the system's default timezone
   */
  public static ZoneId getSystemTimezone() {
    return SYSTEM_TIMEZONE;
  }

  /**
   * Parse timestamp from long value (milliseconds).
   *
   * @param timestampMillis timestamp in milliseconds since epoch
   * @return the Date object representing the timestamp
   */
  public static Date parseTimestamp(long timestampMillis) {
    return new Date(timestampMillis);
  }

  /**
   * Convert date to system timezone if it was parsed from a different timezone.
   *
   * @param date the date to convert
   * @param originalString the original date string (used to detect timezone info)
   * @return the converted date in system timezone
   */
  private static Date convertToSystemTimezone(Date date, String originalString) {
    if (date == null || originalString == null) {
      return date;
    }

    try {
      // If the original string contains timezone info, convert to system timezone
      if (originalString.matches(".*[+-]\\d{2}:?\\d{2}$") || originalString.endsWith("Z")) {
        // Parse as ZonedDateTime to handle timezone conversion
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(originalString, formatter);
        ZonedDateTime systemZoned = zonedDateTime.withZoneSameInstant(SYSTEM_TIMEZONE);
        return Date.from(systemZoned.toInstant());
      }
    } catch (Exception e) {
      // If conversion fails, return original date
    }

    return date;
  }

  /**
   * Fallback parsing using Java 8 Time API for additional date format support.
   *
   * @param dateString the date string to parse
   * @return the parsed Date object, or null if all attempts fail
   */
  private static Date tryParseWithTimeAPI(String dateString) {
    if (dateString == null || dateString.trim().isEmpty()) {
      return null;
    }

    // Common additional patterns
    String[] additionalPatterns = {
      "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
      "yyyy-MM-dd'T'HH:mm:ss.SSS",
      "yyyy-MM-dd'T'HH:mm:ss",
      "yyyy-MM-dd'T'HH:mm",
      "yyyy/MM/dd HH:mm:ss",
      "yyyy/MM/dd",
      "dd/MM/yyyy HH:mm:ss",
      "dd/MM/yyyy",
      "dd-MM-yyyy HH:mm:ss",
      "dd-MM-yyyy",
      "MM/dd/yyyy HH:mm:ss",
      "MM/dd/yyyy"
    };

    for (String pattern : additionalPatterns) {
      try {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        if (pattern.contains("HH")) {
          // Has time component
          LocalDateTime localDateTime = LocalDateTime.parse(dateString, formatter);
          return Date.from(localDateTime.atZone(SYSTEM_TIMEZONE).toInstant());
        } else {
          // Date only - add default time
          LocalDateTime localDateTime =
              LocalDateTime.parse(
                  dateString + " 00:00:00", DateTimeFormatter.ofPattern(pattern + " HH:mm:ss"));
          return Date.from(localDateTime.atZone(SYSTEM_TIMEZONE).toInstant());
        }
      } catch (DateTimeParseException e) {
        // Continue to next pattern
      }
    }

    return null; // All parsing attempts failed
  }

  /**
   * Check if string represents a numeric timestamp.
   *
   * @param input the string to check
   * @return true if the string is a numeric timestamp
   */
  private static boolean isNumericTimestamp(String input) {
    if (input == null || input.isEmpty()) {
      return false;
    }

    // Check if all characters are digits (allowing for negative timestamps)
    return input.matches("^-?\\d+$");
  }

  /**
   * Parse timestamp from string (handles both milliseconds and seconds since epoch).
   *
   * @param timestampString the timestamp string to parse
   * @return the parsed Date object, or null if parsing fails
   */
  private static Date parseTimestamp(String timestampString) {
    if (timestampString == null || timestampString.trim().isEmpty()) {
      return null;
    }

    try {
      long timestamp = Long.parseLong(timestampString.trim());

      // Determine if it's milliseconds or seconds based on the value
      // Timestamps in seconds are typically 10 digits (until year 2286)
      // Timestamps in milliseconds are typically 13 digits
      if (String.valueOf(Math.abs(timestamp)).length() <= 10) {
        // Likely seconds since epoch - convert to milliseconds
        timestamp = timestamp * 1000;
      }
      // If >= 13 digits, assume it's already in milliseconds

      return new Date(timestamp);

    } catch (NumberFormatException e) {
      return null;
    }
  }
}

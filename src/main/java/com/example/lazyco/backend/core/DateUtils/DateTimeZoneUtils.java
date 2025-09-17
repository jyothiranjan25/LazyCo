package com.example.lazyco.backend.core.DateUtils;

import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/** Modern, thread-safe date/time utilities with deterministic test mode. */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateTimeZoneUtils {

  /**
   * Check if TEST_MODE is enabled via system property. In test mode, current time is fixed to a
   * specific instant for deterministic testing.
   */
  private static boolean isTestMode() {
    return Boolean.parseBoolean(System.getProperty("TEST_MODE", "false"));
  }

  /**
   * If TEST_MODE is enabled, this reads the fixed time from the system property TEST_FIXED_TIME
   * Defaults to current instant if the property is absent or invalid.
   */
  private static Instant fixedInstant() {
    String value = System.getProperty("TEST_FIXED_TIME");
    if (value == null || value.trim().isEmpty()) {
      return Instant.now();
    }

    // 1️⃣ First try strict ISO instant (e.g. 2024-10-01T10:15:30Z)
    try {
      return Instant.parse(value.trim());
    } catch (DateTimeParseException ignored) {
      // continue to next strategy
    }

    // 2️⃣ Fallback: use DateParser to handle plain dates or other supported formats
    Date parsed = DateParser.deserializeDate(value);
    if (parsed != null) {
      return parsed.toInstant();
    }

    ApplicationLogger.error("Invalid TEST_FIXED_TIME '{}', falling back to current time.", value);
    return Instant.now();
  }

  /** Centralized "now" supplier respecting test mode */
  private static Instant nowInstant() {
    return isTestMode() ? fixedInstant() : Instant.now();
  }

  /**
   * Get system timezone from system property, environment variable, or default. Supports multiple
   * ways to configure timezone for different deployment environments.
   */
  public static ZoneId getClientTimezone() {
    String timezoneProperty = System.getProperty("CLIENT_TIMEZONE");
    return DateTimeProps.getSystemTimezone(timezoneProperty);
  }

  private static ZoneId systemZone() {
    return DateParser.getSystemTimezone();
  }

  public static Instant now() {
    return nowInstant();
  }

  public static LocalDate currentDate() {
    return nowInstant().atZone(systemZone()).toLocalDate();
  }

  public static LocalDateTime currentDateTime() {
    return nowInstant().atZone(systemZone()).toLocalDateTime();
  }

  public static OffsetDateTime currentOffsetDateTime() {
    return nowInstant().atZone(systemZone()).toOffsetDateTime();
  }

  public static boolean isValidFormat(String pattern, String value) {
    if (pattern == null || value == null) return false;
    try {
      DateTimeFormatter f = DateTimeFormatter.ofPattern(pattern).withZone(systemZone());
      Instant parsed = f.parse(value, Instant::from);
      return f.format(parsed).equals(value);
    } catch (Exception e) {
      ApplicationLogger.debug(
          "Invalid date '{}' for pattern '{}': {}", value, pattern, e.toString());
      return false;
    }
  }

  public static Instant parseInstant(String value, String pattern) {
    Objects.requireNonNull(value, "value");
    Objects.requireNonNull(pattern, "pattern");
    DateTimeFormatter f = DateTimeFormatter.ofPattern(pattern).withZone(systemZone());
    return f.parse(value, Instant::from);
  }

  public static boolean isPast(Instant instant) {
    return instant.isBefore(nowInstant());
  }

  public static Duration diff(Instant start, Instant end) {
    return Duration.between(start, end);
  }

  public static boolean rangesOverlap(Instant start1, Instant end1, Instant start2, Instant end2) {
    return !end1.isBefore(start2) && !end2.isBefore(start1);
  }

  public static boolean rangesOverlapExclusive(
      Instant start1, Instant end1, Instant start2, Instant end2) {
    return start1.isBefore(end2) && start2.isBefore(end1);
  }

  public static Instant addDays(Instant instant, int days) {
    return instant.plus(Duration.ofDays(days));
  }

  public static DayOfWeek dayOfWeek(Instant instant) {
    return instant.atZone(systemZone()).getDayOfWeek();
  }

  public static Instant startOfDay(LocalDate date) {
    return date.atStartOfDay(systemZone()).toInstant();
  }

  public static Instant endOfDay(LocalDate date) {
    return date.atTime(LocalTime.MAX).atZone(systemZone()).toInstant();
  }

  public static Date startOfDay(Date date) {
    if (date == null) return null;
    Instant instant = date.toInstant();
    LocalDate localDate = instant.atZone(systemZone()).toLocalDate();
    Instant startOfDay = startOfDay(localDate);
    return Date.from(startOfDay);
  }

  public static Date endOfDay(Date date) {
    if (date == null) return null;
    Instant instant = date.toInstant();
    LocalDate localDate = instant.atZone(systemZone()).toLocalDate();
    Instant endOfDay = endOfDay(localDate);
    return Date.from(endOfDay);
  }

  public static LocalDate toLocalDate(Date date) {
    if (date == null) return null;
    return date.toInstant().atZone(systemZone()).toLocalDate();
  }

  public static List<LocalDate> weeklyOccurrencesBetween(
      LocalDate start, LocalDate end, DayOfWeek day) {

    List<LocalDate> list = new ArrayList<>();
    LocalDate d = start.with(TemporalAdjusters.nextOrSame(day));
    while (!d.isAfter(end)) {
      list.add(d);
      d = d.plusWeeks(1);
    }
    return list;
  }

  public static Instant convertZone(Instant instant, ZoneId from, ZoneId to) {
    return instant.atZone(from).withZoneSameInstant(to).toInstant();
  }

  public static Instant toSystemZone(Instant clientInstant) {
    return convertZone(clientInstant, getClientTimezone(), systemZone());
  }

  public static Instant toClientZone(Instant systemInstant) {
    return convertZone(systemInstant, systemZone(), getClientTimezone());
  }
}

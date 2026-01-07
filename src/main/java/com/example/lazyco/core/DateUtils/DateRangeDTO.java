package com.example.lazyco.core.DateUtils;

import com.example.lazyco.core.Exceptions.ExceptionWrapper;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
public class DateRangeDTO {

  private Date start;
  private Date end;

  /** Default constructor creates a range for the current day (start of day to end of day). */
  public DateRangeDTO() {
    Date now = DateTimeZoneUtils.getCurrentDate();
    this.start = DateTimeZoneUtils.startOfDay(now);
    this.end = DateTimeZoneUtils.endOfDay(now);
  }

  /**
   * Constructor with explicit start and end dates.
   *
   * @param start the start date (inclusive), must not be null
   * @param end the end date (inclusive), must not be null
   */
  public DateRangeDTO(Date start, Date end) {
    setStart(start);
    setEnd(end);
    validateRange();
  }

  /**
   * Constructor with explicit start and end dates.
   *
   * @param start the start date (inclusive), must not be null
   * @param end the end date (inclusive), must not be null
   */
  public DateRangeDTO(String start, String end) {
    if (StringUtils.isEmpty(start) || StringUtils.isEmpty(end)) {
      return;
    }
    Date startDate = DateParser.deserializeDate(start);
    Date endDate = DateParser.deserializeDate(end);

    if (startDate == null || endDate == null) {
      throw new ExceptionWrapper("Invalid date format");
    }

    setStart(startDate);
    setEnd(endDate);
    validateRange();
  }

  /**
   * Constructor using LocalDate with system timezone.
   *
   * @param startDate the start date, must not be null
   * @param endDate the end date, must not be null
   */
  public DateRangeDTO(LocalDate startDate, LocalDate endDate) {
    this(startDate, endDate, DateTimeZoneUtils.getClientTimezone());
  }

  /**
   * Constructor using LocalDate with specified timezone.
   *
   * @param startDate the start date, must not be null
   * @param endDate the end date, must not be null
   * @param zoneId the timezone to use, must not be null
   */
  public DateRangeDTO(LocalDate startDate, LocalDate endDate, ZoneId zoneId) {
    Objects.requireNonNull(startDate, "Start date cannot be null");
    Objects.requireNonNull(endDate, "End date cannot be null");
    Objects.requireNonNull(zoneId, "Zone ID cannot be null");

    if (endDate.isBefore(startDate)) {
      throw new ExceptionWrapper("End date cannot be before start date");
    }

    this.start = Date.from(DateTimeZoneUtils.startOfDay(startDate, zoneId));
    this.end = Date.from(DateTimeZoneUtils.endOfDay(endDate, zoneId));
  }

  /**
   * Set the start date to the start of the specified day.
   *
   * @param start the date to set the start of day, must not be null
   */
  public void startOfDay(Date start) {
    Objects.requireNonNull(start, "Start date cannot be null");
    this.start = DateTimeZoneUtils.startOfDay(start);
    if (this.end != null) {
      validateRange();
    }
  }

  /**
   * Set the end date to the end of the specified day.
   *
   * @param end the date to set the end of day, must not be null
   */
  public void endOfDay(Date end) {
    Objects.requireNonNull(end, "End date cannot be null");
    this.end = DateTimeZoneUtils.endOfDay(end);
    if (this.start != null) {
      validateRange();
    }
  }

  /**
   * Set the start date with validation.
   *
   * @param start the start date, must not be null
   */
  public void setStart(Date start) {
    Objects.requireNonNull(start, "Start date cannot be null");
    this.start = start;
    if (this.end != null) {
      validateRange();
    }
  }

  /**
   * Set the end date with validation.
   *
   * @param end the end date, must not be null
   */
  public void setEnd(Date end) {
    Objects.requireNonNull(end, "End date cannot be null");
    this.end = end;
    if (this.start != null) {
      validateRange();
    }
  }

  /**
   * Check if this range contains the specified date.
   *
   * @param date the date to check, must not be null
   * @return true if the date falls within this range (inclusive)
   */
  public boolean contains(Date date) {
    Objects.requireNonNull(date, "Date cannot be null");
    return !date.before(start) && !date.after(end);
  }

  /**
   * Check if this range contains the specified instant.
   *
   * @param instant the instant to check, must not be null
   * @return true if the instant falls within this range (inclusive)
   */
  public boolean contains(Instant instant) {
    Objects.requireNonNull(instant, "Instant cannot be null");
    return contains(Date.from(instant));
  }

  /**
   * Check if this range overlaps with another range.
   *
   * @param other the other range to check, must not be null
   * @return true if the ranges overlap
   */
  public boolean overlaps(DateRangeDTO other) {
    Objects.requireNonNull(other, "Other range cannot be null");
    return DateTimeZoneUtils.rangesOverlap(
        start.toInstant(), end.toInstant(),
        other.start.toInstant(), other.end.toInstant());
  }

  /**
   * Check if this range is entirely before another range.
   *
   * @param other the other range to check, must not be null
   * @return true if this range is entirely before the other
   */
  public boolean isBefore(DateRangeDTO other) {
    Objects.requireNonNull(other, "Other range cannot be null");
    return end.before(other.start);
  }

  /**
   * Check if this range is entirely after another range.
   *
   * @param other the other range to check, must not be null
   * @return true if this range is entirely after the other
   */
  public boolean isAfter(DateRangeDTO other) {
    Objects.requireNonNull(other, "Other range cannot be null");
    return start.after(other.end);
  }

  /**
   * Get the duration of this range.
   *
   * @return the duration between start and end
   */
  public Duration getDuration() {
    return Duration.between(start.toInstant(), end.toInstant());
  }

  /**
   * Get the number of days in this range.
   *
   * @return the number of days (can be fractional)
   */
  public double getDurationInDays() {
    return getDuration().toMillis() / (24.0 * 60.0 * 60.0 * 1000.0);
  }

  /**
   * Get the number of full days in this range.
   *
   * @return the number of full days
   */
  public long getFullDays() {
    return getDuration().toDays();
  }

  /**
   * Check if this range represents a single day.
   *
   * @return true if start and end are on the same day
   */
  public boolean isSingleDay() {
    LocalDate startDate = DateTimeZoneUtils.toLocalDate(start);
    LocalDate endDate = DateTimeZoneUtils.toLocalDate(end);
    return startDate.equals(endDate);
  }

  /**
   * Check if this range is in the past (end date is before current time).
   *
   * @return true if the entire range is in the past
   */
  public boolean isPast() {
    return DateTimeZoneUtils.isPast(end.toInstant());
  }

  /**
   * Check if this range is in the future (start date is after current time).
   *
   * @return true if the entire range is in the future
   */
  public boolean isFuture() {
    return DateTimeZoneUtils.isFuture(start.toInstant());
  }

  /**
   * Check if this range contains the current time.
   *
   * @return true if current time falls within this range
   */
  public boolean containsNow() {
    return contains(DateTimeZoneUtils.now());
  }

  /**
   * Create a new range that represents the intersection of this range with another.
   *
   * @param other the other range to intersect with, must not be null
   * @return new DateRangeDTO representing the intersection, or null if no overlap
   */
  public DateRangeDTO intersect(DateRangeDTO other) {
    Objects.requireNonNull(other, "Other range cannot be null");

    if (!overlaps(other)) {
      return null;
    }

    Date intersectionStart = start.after(other.start) ? start : other.start;
    Date intersectionEnd = end.before(other.end) ? end : other.end;

    return new DateRangeDTO(intersectionStart, intersectionEnd);
  }

  /**
   * Create a new range that represents the union of this range with another. Note: This only works
   * for overlapping or adjacent ranges.
   *
   * @param other the other range to union with, must not be null
   * @return new DateRangeDTO representing the union
   */
  public DateRangeDTO union(DateRangeDTO other) {
    Objects.requireNonNull(other, "Other range cannot be null");

    // Check if ranges overlap or are adjacent
    Date thisEndPlusOne = new Date(end.getTime() + 1);
    Date otherEndPlusOne = new Date(other.end.getTime() + 1);

    boolean adjacentOrOverlap = !other.start.after(thisEndPlusOne) && !start.after(otherEndPlusOne);
    if (!adjacentOrOverlap) {
      throw new ExceptionWrapper("Ranges do not overlap or touch; cannot form a union");
    }

    Date unionStart = start.before(other.start) ? start : other.start;
    Date unionEnd = end.after(other.end) ? end : other.end;

    return new DateRangeDTO(unionStart, unionEnd);
  }

  /** Check if this range is adjacent to another (end of one is start of the other). */
  private boolean areAdjacent(DateRangeDTO other) {
    return end.getTime() + 1 == other.start.getTime() || other.end.getTime() + 1 == start.getTime();
  }

  /** Validate that start is not after end. */
  private void validateRange() {
    if (end.before(start)) {
      throw new ExceptionWrapper("End date cannot be before start date");
    }
  }
}

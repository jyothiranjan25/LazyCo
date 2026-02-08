package com.example.lazyco.core.DateUtils;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocalDateTimeRangeDTO {
  private LocalDateTime start;
  private LocalDateTime end;

  public LocalDateTimeRangeDTO() {
    LocalDateTime now = LocalDateTime.now();
    this.start = now;
    this.end = now;
  }

  public LocalDateTimeRangeDTO(LocalDateTime start, LocalDateTime end) {
    this.start = start;
    this.end = end;
    validateRange();
  }

  private void validateRange() {
    if (start != null && end != null && start.isAfter(end)) {
      throw new IllegalArgumentException("Start date must be before or equal to end date");
    }
  }
}

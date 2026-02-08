package com.example.lazyco.core.DateUtils;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocalDateRangeDTO {
  private LocalDate start;
  private LocalDate end;

  public LocalDateRangeDTO() {
    LocalDate now = LocalDate.now();
    this.start = now;
    this.end = now;
  }

  public LocalDateRangeDTO(LocalDate start, LocalDate end) {
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

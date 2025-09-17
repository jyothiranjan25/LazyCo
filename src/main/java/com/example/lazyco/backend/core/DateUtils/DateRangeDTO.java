package com.example.lazyco.backend.core.DateUtils;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DateRangeDTO {
  private Date start;
  private Date end;

  public DateRangeDTO() {
    this.start = DateTimeZoneUtils.startOfDay(new Date());
    this.end = DateTimeZoneUtils.endOfDay(new Date());
  }

  DateRangeDTO(Date start, Date end) {
    this.start = start;
    this.end = end;
  }
}

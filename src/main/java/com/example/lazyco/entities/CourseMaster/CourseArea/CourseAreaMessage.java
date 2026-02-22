package com.example.lazyco.entities.CourseMaster.CourseArea;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum CourseAreaMessage implements MessageCodes {
  COURSE_AREA_NAME_REQUIRED("COURSE_AREA.COURSE_AREA_NAME_REQUIRED"),
  DUPLICATE_COURSE_AREA("COURSE_AREA.DUPLICATE_COURSE_AREA");

  private final String value;

  CourseAreaMessage(String value) {
    this.value = value;
  }
}

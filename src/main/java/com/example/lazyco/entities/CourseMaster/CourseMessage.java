package com.example.lazyco.entities.CourseMaster;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum CourseMessage implements MessageCodes {
  CODE_IS_REQUIRED("COURSE.CODE_IS_REQUIRED"),
  NAME_IS_REQUIRED("COURSE.NAME_IS_REQUIRED");

  private final String value;

  CourseMessage(String value) {
    this.value = value;
  }
}

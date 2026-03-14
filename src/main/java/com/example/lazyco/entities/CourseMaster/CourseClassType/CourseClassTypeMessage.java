package com.example.lazyco.entities.CourseMaster.CourseClassType;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum CourseClassTypeMessage implements MessageCodes {
  ;

  private final String value;

  CourseClassTypeMessage(String value) {
    this.value = value;
  }
}

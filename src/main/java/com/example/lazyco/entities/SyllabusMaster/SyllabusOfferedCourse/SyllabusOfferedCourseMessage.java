package com.example.lazyco.entities.SyllabusMaster.SyllabusOfferedCourse;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum SyllabusOfferedCourseMessage implements MessageCodes {
  ;

  private final String value;

  SyllabusOfferedCourseMessage(String value) {
    this.value = value;
  }
}

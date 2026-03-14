package com.example.lazyco.entities.CourseMaster.CourseRequisite;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum CourseRequisiteMessage implements MessageCodes {
  ;

  private final String value;

  CourseRequisiteMessage(String value) {
    this.value = value;
  }
}

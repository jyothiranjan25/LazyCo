package com.example.lazyco.entities.CourseMaster.CourseCredit;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum CourseCreditMessage implements MessageCodes {
  ;

  private final String value;

  CourseCreditMessage(String value) {
    this.value = value;
  }
}

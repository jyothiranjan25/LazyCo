package com.example.lazyco.entities.SyllabusMaster.CourseCategory;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum CourseCategoryMessage implements MessageCodes {
  ;

  private final String value;

  CourseCategoryMessage(String value) {
    this.value = value;
  }
}

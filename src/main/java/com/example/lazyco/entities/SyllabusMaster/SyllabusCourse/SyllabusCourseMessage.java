package com.example.lazyco.entities.SyllabusMaster.SyllabusCourse;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum SyllabusCourseMessage implements MessageCodes {
  ;

  private final String value;

  SyllabusCourseMessage(String value) {
    this.value = value;
  }
}

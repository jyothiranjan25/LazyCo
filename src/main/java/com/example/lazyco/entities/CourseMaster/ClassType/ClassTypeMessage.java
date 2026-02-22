package com.example.lazyco.entities.CourseMaster.ClassType;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum ClassTypeMessage implements MessageCodes {
  CLASS_TYPE_NAME_REQUIRED("CLASS_TYPE.CLASS_TYPE_NAME_REQUIRED"),
  DUPLICATE_CLASS_TYPE("CLASS_TYPE.DUPLICATE_CLASS_TYPE");

  private final String value;

  ClassTypeMessage(String value) {
    this.value = value;
  }
}

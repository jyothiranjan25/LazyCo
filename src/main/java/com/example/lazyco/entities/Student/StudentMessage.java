package com.example.lazyco.entities.Student;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum StudentMessage implements MessageCodes {
  FIRST_NAME_IS_REQUIRED("STUDENT.FIRST_NAME_IS_REQUIRED"),
  EMAIL_IS_REQUIRED("STUDENT.EMAIL_IS_REQUIRED"),
  PHONE_NUMBER_IS_REQUIRED("STUDENT.PHONE_NUMBER_IS_REQUIRED");

  private final String value;

  StudentMessage(String value) {
    this.value = value;
  }
}

package com.example.lazyco.entities.Student.StudentCustomField;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum StudentCustomFieldMessage implements MessageCodes {
  ;

  private final String value;

  StudentCustomFieldMessage(String value) {
    this.value = value;
  }
}

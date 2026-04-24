package com.example.lazyco.entities.StudentFormStructure.StudentFormPage;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum StudentFormPageMessage implements MessageCodes {
  ;

  private final String value;

  StudentFormPageMessage(String value) {
    this.value = value;
  }
}

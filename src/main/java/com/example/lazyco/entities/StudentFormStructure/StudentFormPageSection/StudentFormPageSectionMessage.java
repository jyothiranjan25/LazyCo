package com.example.lazyco.entities.StudentFormStructure.StudentFormPageSection;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum StudentFormPageSectionMessage implements MessageCodes {
  ;

  private final String value;

  StudentFormPageSectionMessage(String value) {
    this.value = value;
  }
}

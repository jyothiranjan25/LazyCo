package com.example.lazyco.entities.StudentFormStructure.StudentFormSectionCustomField;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum StudentFormSectionCustomFieldMessage implements MessageCodes {
  ;

  private final String value;

  StudentFormSectionCustomFieldMessage(String value) {
    this.value = value;
  }
}

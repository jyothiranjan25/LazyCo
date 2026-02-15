package com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormSectionCustomField;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum ApplicationFormSectionCustomFieldMessage implements MessageCodes {
  ;

  private final String value;

  ApplicationFormSectionCustomFieldMessage(String value) {
    this.value = value;
  }
}

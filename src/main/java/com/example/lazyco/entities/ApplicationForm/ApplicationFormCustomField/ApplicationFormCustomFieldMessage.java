package com.example.lazyco.entities.ApplicationForm.ApplicationFormCustomField;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum ApplicationFormCustomFieldMessage implements MessageCodes {
  ;

  private final String value;

  ApplicationFormCustomFieldMessage(String value) {
    this.value = value;
  }
}

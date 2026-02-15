package com.example.lazyco.entities.CustomField;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum CustomFieldMessage implements MessageCodes {
  CUSTOM_FIELD_NAME_REQUIRED("CUSTOM_FIELD.CUSTOM_FIELD_NAME_REQUIRED"),
  ;

  private final String value;

  CustomFieldMessage(String value) {
    this.value = value;
  }
}

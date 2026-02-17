package com.example.lazyco.entities.CustomField;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum CustomFieldMessage implements MessageCodes {
  CUSTOM_FIELD_NAME_REQUIRED("CUSTOM_FIELD.CUSTOM_FIELD_NAME_REQUIRED"),
  CUSTOM_FIELD_KEY_ALREADY_EXISTS("CUSTOM_FIELD.CUSTOM_FIELD_KEY_ALREADY_EXISTS"),
  CUSTOM_FIELD_TYPE_REQUIRED("CUSTOM_FIELD.CUSTOM_FIELD_TYPE_REQUIRED"),
  UPDATE_CUSTOM_FIELD_TYPE_NOT_ALLOWED("CUSTOM_FIELD.UPDATE_CUSTOM_FIELD_TYPE_NOT_ALLOWED"),
  ;

  private final String value;

  CustomFieldMessage(String value) {
    this.value = value;
  }
}

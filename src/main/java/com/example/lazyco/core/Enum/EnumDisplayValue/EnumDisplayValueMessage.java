package com.example.lazyco.core.Enum.EnumDisplayValue;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum EnumDisplayValueMessage implements MessageCodes {
  MANDATORY_FIELDS_MISSING("ENUM_DISPLAY_VALUE.MANDATORY_FIELDS_MISSING"),
  DUPLICATE_ENUM_CODE("ENUM_DISPLAY_VALUE.DUPLICATE_ENUM_CODE"),
  INVALID_ENUM_CODE("ENUM_DISPLAY_VALUE.INVALID_ENUM_CODE"),
  ENUM_DISPLAY_VALUE_NOT_FOUND("ENUM_DISPLAY_VALUE.ENUM_DISPLAY_VALUE_NOT_FOUND"),
  DUPLICATE_DISPLAY_VALUE("ENUM_DISPLAY_VALUE.DUPLICATE_DISPLAY_VALUE"),
  ;
  private final String value;

  EnumDisplayValueMessage(String value) {
    this.value = value;
  }
}

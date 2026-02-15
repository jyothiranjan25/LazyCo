package com.example.lazyco.entities.CustomField.CustomFieldOption;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum CustomFieldOptionMessage implements MessageCodes {
  ;

  private final String value;

  CustomFieldOptionMessage(String value) {
    this.value = value;
  }
}

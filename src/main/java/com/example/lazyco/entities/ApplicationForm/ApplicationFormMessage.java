package com.example.lazyco.entities.ApplicationForm;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum ApplicationFormMessage implements MessageCodes {
  ;

  private final String value;

  ApplicationFormMessage(String value) {
    this.value = value;
  }
}

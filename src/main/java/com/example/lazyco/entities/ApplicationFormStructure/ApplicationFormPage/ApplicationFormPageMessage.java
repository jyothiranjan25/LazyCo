package com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormPage;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum ApplicationFormPageMessage implements MessageCodes {
  ;

  private final String value;

  ApplicationFormPageMessage(String value) {
    this.value = value;
  }
}

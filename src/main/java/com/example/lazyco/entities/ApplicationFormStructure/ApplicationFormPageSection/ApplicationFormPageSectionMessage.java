package com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormPageSection;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum ApplicationFormPageSectionMessage implements MessageCodes {
  ;

  private final String value;

  ApplicationFormPageSectionMessage(String value) {
    this.value = value;
  }
}

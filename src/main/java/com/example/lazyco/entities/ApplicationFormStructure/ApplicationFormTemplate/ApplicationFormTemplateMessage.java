package com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormTemplate;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum ApplicationFormTemplateMessage implements MessageCodes {
  ;

  private final String value;

  ApplicationFormTemplateMessage(String value) {
    this.value = value;
  }
}

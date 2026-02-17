package com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormTemplateDocument;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum ApplicationFormTemplateDocumentMessage implements MessageCodes {
  ;

  private final String value;

  ApplicationFormTemplateDocumentMessage(String value) {
    this.value = value;
  }
}

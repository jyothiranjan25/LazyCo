package com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormDocument;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum ApplicationFormDocumentMessage implements MessageCodes {
  ;

  private final String value;

  ApplicationFormDocumentMessage(String value) {
    this.value = value;
  }
}

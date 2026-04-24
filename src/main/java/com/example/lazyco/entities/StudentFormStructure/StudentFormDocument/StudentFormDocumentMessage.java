package com.example.lazyco.entities.StudentFormStructure.StudentFormDocument;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum StudentFormDocumentMessage implements MessageCodes {
  ;

  private final String value;

  StudentFormDocumentMessage(String value) {
    this.value = value;
  }
}

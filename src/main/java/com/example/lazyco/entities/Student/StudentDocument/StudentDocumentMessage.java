package com.example.lazyco.entities.Student.StudentDocument;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum StudentDocumentMessage implements MessageCodes {
  ;

  private final String value;

  StudentDocumentMessage(String value) {
    this.value = value;
  }
}

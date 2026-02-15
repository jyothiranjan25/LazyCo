package com.example.lazyco.entities.Document;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum DocumentMessage implements MessageCodes {
  DOCUMENT_NAME_REQUIRED("DOCUMENT.DOCUMENT_NAME_REQUIRED"),
  DOCUMENT_TYPE_REQUIRED("DOCUMENT.DOCUMENT_TYPE_REQUIRED"),
  DUPLICATE_DOCUMENT_NAME("DOCUMENT.DUPLICATE_DOCUMENT_NAME"),
  ;
  private final String value;

  DocumentMessage(String value) {
    this.value = value;
  }
}

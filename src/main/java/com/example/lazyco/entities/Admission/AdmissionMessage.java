package com.example.lazyco.entities.Admission;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum AdmissionMessage implements MessageCodes {
  ;

  private final String value;

  AdmissionMessage(String value) {
    this.value = value;
  }
}

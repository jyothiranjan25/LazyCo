package com.example.lazyco.backend.entities.UserManagement.Sample;

import com.example.lazyco.backend.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum SampleMessage implements MessageCodes {
  ;

  private final String value;

  SampleMessage(String value) {
    this.value = value;
  }
}

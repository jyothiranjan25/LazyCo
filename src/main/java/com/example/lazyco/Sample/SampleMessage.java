package com.example.lazyco.Sample;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum SampleMessage implements MessageCodes {
  ;

  private final String value;

  SampleMessage(String value) {
    this.value = value;
  }
}

package com.example.lazyco.entities.TermCycle;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum TermCycleMessage implements MessageCodes {
  ;

  private final String value;

  TermCycleMessage(String value) {
    this.value = value;
  }
}

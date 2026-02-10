package com.example.lazyco.entities.ProgramCycle;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum ProgramCycleMessage implements MessageCodes {
  ;

  private final String value;

  ProgramCycleMessage(String value) {
    this.value = value;
  }
}

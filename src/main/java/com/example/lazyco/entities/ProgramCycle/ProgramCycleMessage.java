package com.example.lazyco.entities.ProgramCycle;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum ProgramCycleMessage implements MessageCodes {
  PROGRAM_CYCLE_CODE_REQUIRED("PROGRAM_CYCLE.PROGRAM_CYCLE_CODE_REQUIRED");

  private final String value;

  ProgramCycleMessage(String value) {
    this.value = value;
  }
}

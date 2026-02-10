package com.example.lazyco.entities.TermCycle;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum TermCycleMessage implements MessageCodes {
  TERM_CYCLE_ACADEMIC_YEAR_ID_REQUIRED("TERM_CYCLE.ACADEMIC_YEAR_ID_REQUIRED"),
  TERM_CYCLE_TERM_MASTER_ID_REQUIRED("TERM_CYCLE.TERM_MASTER_ID_REQUIRED"),
  TERM_CYCLE_CODE_REQUIRED("TERM_CYCLE.CODE_REQUIRED"),
  TERM_CYCLE_NAME_REQUIRED("TERM_CYCLE.NAME_REQUIRED"),
  DUPLICATE_TERM_CYCLE_NAME("TERM_CYCLE.DUPLICATE_NAME");

  private final String value;

  TermCycleMessage(String value) {
    this.value = value;
  }
}

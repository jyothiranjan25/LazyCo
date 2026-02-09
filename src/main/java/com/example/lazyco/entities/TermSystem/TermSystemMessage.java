package com.example.lazyco.entities.TermSystem;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum TermSystemMessage implements MessageCodes {
  TERM_SYSTEM_CODE_REQUIRED("TERM_SYSTEM.TERM_SYSTEM_CODE_REQUIRED"),
  TERM_SYSTEM_NAME_REQUIRED("TERM_SYSTEM.TERM_SYSTEM_NAME_REQUIRED"),
  DUPLICATE_TERM_SYSTEM_NAME("TERM_SYSTEM.DUPLICATE_TERM_SYSTEM_NAME"),
  TERM_SYSTEM_TERM_MASTERS_REQUIRED("TERM_SYSTEM.TERM_SYSTEM_TERM_MASTERS_REQUIRED"),
  ERROR_CREATING_TERM_MASTERS("TERM_SYSTEM.ERROR_CREATING_TERM_MASTERS");

  private final String value;

  TermSystemMessage(String value) {
    this.value = value;
  }
}

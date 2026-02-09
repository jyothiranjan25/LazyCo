package com.example.lazyco.entities.TermSystem.TermMaster;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum TermMasterMessage implements MessageCodes {
  TERM_MASTER_TERM_SYSTEM_ID_REQUIRED("TERM_MASTER.TERM_MASTER_TERM_SYSTEM_ID_REQUIRED"),
  TERM_MASTER_CODE_REQUIRED("TERM_MASTER.TERM_MASTER_CODE_REQUIRED"),
  TERM_MASTER_NAME_REQUIRED("TERM_MASTER.TERM_SYSTEM_NAME_REQUIRED"),
  DUPLICATE_TERM_MASTER_NAME("TERM_MASTER.DUPLICATE_TERM_SYSTEM_NAME"),
  ;

  private final String value;

  TermMasterMessage(String value) {
    this.value = value;
  }
}

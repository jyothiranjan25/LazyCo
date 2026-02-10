package com.example.lazyco.entities.AcademicProgram.ProgramTermMaster;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum ProgramTermMasterMessage implements MessageCodes {
  PROGRAM_TERM_SYSTEM_ID_IS_REQUIRED("PROGRAM_TERM_MASTER.PROGRAM_TERM_SYSTEM_ID_IS_REQUIRED"),
  PROGRAM_TERM_MASTER_CODE_IS_REQUIRED("PROGRAM_TERM_MASTER.PROGRAM_TERM_MASTER_CODE_IS_REQUIRED"),
  PROGRAM_TERM_MASTER_NAME_IS_REQUIRED("PROGRAM_TERM_MASTER.PROGRAM_TERM_MASTER_NAME_IS_REQUIRED"),
  DUPLICATE_PROGRAM_TERM_MASTER_NAME("PROGRAM_TERM_MASTER.DUPLICATE_PROGRAM_TERM_MASTER_NAME");

  private final String value;

  ProgramTermMasterMessage(String value) {
    this.value = value;
  }
}

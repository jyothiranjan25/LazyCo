package com.example.lazyco.entities.AcademicProgram.ProgramTermSystem;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum ProgramTermSystemMessage implements MessageCodes {
  PROGRAM_TERM_SYSTEM_PROGRAM_ID_IS_REQUIRED(
      "PROGRAM_TERM_SYSTEM.PROGRAM_TERM_SYSTEM_PROGRAM_ID_IS_REQUIRED"),
  PROGRAM_TERM_SYSTEM_CODE_IS_REQUIRED("PROGRAM_TERM_SYSTEM.PROGRAM_TERM_SYSTEM_CODE_IS_REQUIRED"),
  PROGRAM_TERM_SYSTEM_NAME_IS_REQUIRED("PROGRAM_TERM_SYSTEM.PROGRAM_TERM_SYSTEM_NAME_IS_REQUIRED"),
  DUPLICATE_PROGRAM_TERM_SYSTEM_NAME("PROGRAM_TERM_SYSTEM.DUPLICATE_PROGRAM_TERM_SYSTEM_NAME");

  private final String value;

  ProgramTermSystemMessage(String value) {
    this.value = value;
  }
}

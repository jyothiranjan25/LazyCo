package com.example.lazyco.entities.AcademicProgram;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum AcademicProgramMessage implements MessageCodes {
  ACADEMIC_PROGRAM_CODE_REQUIRED("ACADEMIC_PROGRAM.ACADEMIC_PROGRAM_CODE_REQUIRED"),
  ACADEMIC_PROGRAM_NAME_REQUIRED("ACADEMIC_PROGRAM.ACADEMIC_PROGRAM_NAME_REQUIRED"),
  DUPLICATE_ACADEMIC_PROGRAM_NAME("ACADEMIC_PROGRAM.DUPLICATE_ACADEMIC_PROGRAM_NAME");

  private final String value;

  AcademicProgramMessage(String value) {
    this.value = value;
  }
}

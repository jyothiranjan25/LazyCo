package com.example.lazyco.entities.AcademicProgram.ProgramSpecialization;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum ProgramSpecializationMessage implements MessageCodes {
  ACADEMIC_PROGRAM_ID_IS_REQUIRED("PROGRAM_SPECIALIZATION.ACADEMIC_PROGRAM_ID_IS_REQUIRED"),
  PROGRAM_SPECIALIZATION_CODE_IS_REQUIRED(
      "PROGRAM_SPECIALIZATION.PROGRAM_SPECIALIZATION_CODE_IS_REQUIRED"),
  PROGRAM_SPECIALIZATION_NAME_IS_REQUIRED(
      "PROGRAM_SPECIALIZATION.PROGRAM_SPECIALIZATION_NAME_IS_REQUIRED"),
  DUPLICATE_PROGRAM_SPECIALIZATION_NAME(
      "PROGRAM_SPECIALIZATION.DUPLICATE_PROGRAM_SPECIALIZATION_NAME");

  private final String value;

  ProgramSpecializationMessage(String value) {
    this.value = value;
  }
}

package com.example.lazyco.entities.Admission;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum AdmissionMessage implements MessageCodes {
  ADMISSION_NUMBER_IS_REQUIRED("ADMISSION.ADMISSION_NUMBER_IS_REQUIRED"),
  PROGRAM_CURRICULUM_REQUIRED("ADMISSION.PROGRAM_CURRICULUM_REQUIRED"),
  JOINING_PROGRAM_CYCLE_REQUIRED("ADMISSION.JOINING_PROGRAM_CYCLE_REQUIRED"),
  ADMISSION_ALREADY_EXISTS_FOR_CURRICULUM("ADMISSION.ADMISSION_ALREADY_EXISTS_FOR_CURRICULUM");

  private final String value;

  AdmissionMessage(String value) {
    this.value = value;
  }
}

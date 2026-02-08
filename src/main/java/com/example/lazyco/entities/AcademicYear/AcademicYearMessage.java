package com.example.lazyco.entities.AcademicYear;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum AcademicYearMessage implements MessageCodes {
  ACADEMIC_YEAR_CODE_REQUIRED("ACADEMIC_YEAR.ACADEMIC_YEAR_CODE_REQUIRED"),
  ACADEMIC_YEAR_NAME_REQUIRED("ACADEMIC_YEAR.ACADEMIC_YEAR_NAME_REQUIRED"),
  ACADEMIC_YEAR_DATE_CONFLICT("ACADEMIC_YEAR.ACADEMIC_YEAR_DATE_CONFLICT");

  private final String value;

  AcademicYearMessage(String value) {
    this.value = value;
  }
}

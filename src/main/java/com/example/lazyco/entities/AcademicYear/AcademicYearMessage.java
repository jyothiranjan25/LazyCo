package com.example.lazyco.entities.AcademicYear;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum AcademicYearMessage implements MessageCodes {
  ;

  private final String value;

  AcademicYearMessage(String value) {
    this.value = value;
  }
}

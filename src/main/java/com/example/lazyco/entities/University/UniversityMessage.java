package com.example.lazyco.entities.University;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum UniversityMessage implements MessageCodes {
  UNIVERSITY_CODE_REQUIRED("UNIVERSITY.UNIVERSITY_CODE_REQUIRED"),
  UNIVERSITY_NAME_REQUIRED("UNIVERSITY.UNIVERSITY_NAME_REQUIRED"),
  DUPLICATE_UNIVERSITY_NAME("UNIVERSITY.DUPLICATE_UNIVERSITY_NAME"),
  ;

  private final String value;

  UniversityMessage(String value) {
    this.value = value;
  }
}

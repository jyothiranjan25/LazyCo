package com.example.lazyco.entities.Institution;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum InstitutionMessage implements MessageCodes {
  UNIVERSITY_ID_REQUIRED("INSTITUTION.UNIVERSITY_ID_REQUIRED"),
  INSTITUTION_CODE_REQUIRED("INSTITUTION.INSTITUTION_CODE_REQUIRED"),
  INSTITUTION_NAME_REQUIRED("INSTITUTION.INSTITUTION_NAME_REQUIRED"),
  DUPLICATE_INSTITUTION_NAME("INSTITUTION.DUPLICATE_INSTITUTION_NAME"),
  ;

  private final String value;

  InstitutionMessage(String value) {
    this.value = value;
  }
}

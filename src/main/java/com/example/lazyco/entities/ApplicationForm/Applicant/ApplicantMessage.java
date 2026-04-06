package com.example.lazyco.entities.ApplicationForm.Applicant;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum ApplicantMessage implements MessageCodes {
  ;

  private final String value;

  ApplicantMessage(String value) {
    this.value = value;
  }
}

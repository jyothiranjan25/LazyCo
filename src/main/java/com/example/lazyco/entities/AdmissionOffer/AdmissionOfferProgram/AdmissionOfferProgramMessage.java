package com.example.lazyco.entities.AdmissionOffer.AdmissionOfferProgram;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum AdmissionOfferProgramMessage implements MessageCodes {
  ;

  private final String value;

  AdmissionOfferProgramMessage(String value) {
    this.value = value;
  }
}

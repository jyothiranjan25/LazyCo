package com.example.lazyco.entities.AdmissionOffer;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum AdmissionOfferMessage implements MessageCodes {
  ;

  private final String value;

  AdmissionOfferMessage(String value) {
    this.value = value;
  }
}

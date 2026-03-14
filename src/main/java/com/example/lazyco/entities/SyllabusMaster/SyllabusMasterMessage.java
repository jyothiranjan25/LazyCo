package com.example.lazyco.entities.SyllabusMaster;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum SyllabusMasterMessage implements MessageCodes {
  ;

  private final String value;

  SyllabusMasterMessage(String value) {
    this.value = value;
  }
}

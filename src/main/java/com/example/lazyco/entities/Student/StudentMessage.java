package com.example.lazyco.entities.Student;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum StudentMessage implements MessageCodes {
  ;

  private final String value;

  StudentMessage(String value) {
    this.value = value;
  }
}

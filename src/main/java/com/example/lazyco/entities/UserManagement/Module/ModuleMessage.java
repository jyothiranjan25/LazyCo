package com.example.lazyco.entities.UserManagement.Module;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum ModuleMessage implements MessageCodes {
  ;

  private final String value;

  ModuleMessage(String value) {
    this.value = value;
  }
}

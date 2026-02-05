package com.example.lazyco.entities.UserManagement.Module;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum ModuleMessage implements MessageCodes {
  MODULE_NAME_REQUIRED("MODULE.MODULE_NAME_REQUIRED"),
  DUPLICATE_MODULE_NAME("MODULE.DUPLICATE_MODULE_NAME"),
  ;

  private final String value;

  ModuleMessage(String value) {
    this.value = value;
  }
}

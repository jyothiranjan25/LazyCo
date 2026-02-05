package com.example.lazyco.entities.UserManagement.Module;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum ModuleMessage implements MessageCodes {
  MODULE_NAME_REQUIRED("MODULE.MODULE_NAME_REQUIRED"),
  MODULE_ACTION_REQUIRED("MODULE.MODULE_ACTION_REQUIRED"),
  DUPLICATE_MODULE_NAME("MODULE.DUPLICATE_MODULE_NAME"),
  SELECT_ACTION_OR_RESOURCE("MODULE.SELECT_ACTION_OR_RESOURCE"),
  ;

  private final String value;

  ModuleMessage(String value) {
    this.value = value;
  }
}

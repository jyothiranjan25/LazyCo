package com.example.lazyco.entities.UserManagement.RoleModuleResource;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum RoleModuleResourceMessage implements MessageCodes {
  ;

  private final String value;

  RoleModuleResourceMessage(String value) {
    this.value = value;
  }
}

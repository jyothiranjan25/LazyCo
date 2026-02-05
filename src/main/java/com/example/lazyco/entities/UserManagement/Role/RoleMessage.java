package com.example.lazyco.entities.UserManagement.Role;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum RoleMessage implements MessageCodes {
  ROLE_NAME_REQUIRED("ROLE.ROLE_NAME_REQUIRED"),
  DUPLICATE_ROLE_NAME("ROLE.DUPLICATE_ROLE_NAME"),
  ;

  private final String value;

  RoleMessage(String value) {
    this.value = value;
  }
}

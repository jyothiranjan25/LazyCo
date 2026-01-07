package com.example.lazyco.backend.entities.UserManagement.Role;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum RoleMessage implements MessageCodes {
  ;

  private final String value;

  RoleMessage(String value) {
    this.value = value;
  }
}

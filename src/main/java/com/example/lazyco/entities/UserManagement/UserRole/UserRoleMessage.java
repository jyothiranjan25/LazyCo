package com.example.lazyco.backend.entities.UserManagement.UserRole;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum UserRoleMessage implements MessageCodes {
  ;

  private final String value;

  UserRoleMessage(String value) {
    this.value = value;
  }
}

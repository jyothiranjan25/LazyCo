package com.example.lazyco.backend.entities.UserManagement.UserGroup;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum UserGroupMessage implements MessageCodes {
  ;
  private final String value;

  UserGroupMessage(String value) {
    this.value = value;
  }
}

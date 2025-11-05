package com.example.lazyco.backend.entities.UserManagement.UserGroup;

import com.example.lazyco.backend.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum UserGroupMessage implements MessageCodes {
  ;
  private final String value;

  UserGroupMessage(String value) {
    this.value = value;
  }
}

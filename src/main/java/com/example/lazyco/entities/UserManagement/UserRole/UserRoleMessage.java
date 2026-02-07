package com.example.lazyco.entities.UserManagement.UserRole;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum UserRoleMessage implements MessageCodes {
  ROLE_ID_REQUIRED("USER_ROLE.ROLE_ID_REQUIRED"),
  APP_USER_ID_REQUIRED("USER_ROLE.APP_USER_ID_REQUIRED"),
  USER_GROUP_ID_REQUIRED("USER_ROLE.USER_GROUP_ID_REQUIRED"),
  USER_ROLE_ALREADY_EXISTS("USER_ROLE.USER_ROLE_ALREADY_EXISTS");

  private final String value;

  UserRoleMessage(String value) {
    this.value = value;
  }
}

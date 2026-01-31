package com.example.lazyco.entities.UserManagement.UserGroup;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum UserGroupMessage implements MessageCodes {
    USER_GROUP_NAME_REQUIRED("USER_GROUP.USER_GROUP_NAME_REQUIRED"),
  DUPLICATE_USER_GROUP_NAME("USER_GROUP.DUPLICATE_USER_GROUP_NAME"),
  USER_GROUP_DELETE_NOT_ALLOWED("USER_GROUP.USER_GROUP_DELETE_NOT_ALLOWED")
  ;
  private final String value;

  UserGroupMessage(String value) {
    this.value = value;
  }
}

package com.example.lazyco.entities.UserManagement.AppUser;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum AppUserMessage implements MessageCodes {
  USER_ID_REQUIRED("APP_USER.USER_ID_REQUIRED"),
  EMAIL_REQUIRED("APP_USER.EMAIL_REQUIRED"),
  DUPLICATE_USER_ID("APP_USER.DUPLICATE_USER_ID"),
  EMAIL_IN_USE("APP_USER.EMAIL_IN_USE"),
  ;

  private final String value;

  AppUserMessage(String value) {
    this.value = value;
  }
}

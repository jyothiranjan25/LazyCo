package com.example.lazyco.backend.entities.UserManagement.AppUser;

import com.example.lazyco.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum AppUserMessage implements MessageCodes {
  USER_ID_REQUIRED("APP_USER.USER_ID_REQUIRED"),
  ;

  private final String value;

  AppUserMessage(String value) {
    this.value = value;
  }
}

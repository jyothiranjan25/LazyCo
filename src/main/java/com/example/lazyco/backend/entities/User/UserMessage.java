package com.example.lazyco.backend.entities.User;

import com.example.lazyco.backend.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum UserMessage implements MessageCodes {
  USER_NOT_FOUND("USER_MODULES.USER_NOT_FOUND"),
  INCORRECT_PASSWORD("USER_MODULES.INCORRECT_PASSWORD"),
  USER_ID_OR_PASSWORD_INCORRECT("USER_MODULES.USER_ID_OR_PASSWORD_INCORRECT"),
  ACCOUNT_LOCKED("USER_MODULES.ACCOUNT_LOCKED"),
  PASSWORD_EXPIRED("USER_MODULES.PASSWORD_EXPIRED"),
  USER_NOT_AUTHORIZED("USER_MODULES.USER_NOT_AUTHORIZED"),
  ROLE_NOT_SELECTED("USER_MODULES.ROLE_NOT_SELECTED"),

  ROLE_NOT_FOUND("USER_MODULES.ROLE_NOT_FOUND"),
  ;
  private final String value;

  UserMessage(String value) {
    this.value = value;
  }
}

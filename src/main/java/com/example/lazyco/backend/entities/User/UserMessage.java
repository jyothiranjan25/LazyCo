package com.example.lazyco.backend.entities.User;

import com.example.lazyco.backend.core.Messages.MessageCodes;
import lombok.Getter;

@Getter
public enum UserMessage implements MessageCodes {
  USER_NOT_FOUND("User not found"),
  INCORRECT_PASSWORD("Invalid credentials"),
  ACCOUNT_LOCKED("Account is locked"),
  PASSWORD_EXPIRED("Password has expired");

  private final String value;

  UserMessage(String value) {
    this.value = value;
  }
}

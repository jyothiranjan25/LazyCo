package com.example.lazyco.backend.core.Exceptions;

import com.example.lazyco.backend.core.Messages.CustomMessage;
import com.example.lazyco.backend.core.Messages.MessageCodes;
import org.springframework.security.core.AuthenticationException;

public class UnauthorizedException extends AuthenticationException {

  public UnauthorizedException(String code) {
    super(code);
  }

  public UnauthorizedException(MessageCodes code) {
    super(CustomMessage.getMessageString(code));
  }
}

package com.example.lazyco.backend.core.Exceptions;

import com.example.lazyco.backend.core.Messages.MessageCodes;

public class UnauthorizedException extends ExceptionWrapper {

  public UnauthorizedException(MessageCodes code) {
    super(code);
  }
}

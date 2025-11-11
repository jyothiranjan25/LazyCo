package com.example.lazyco.backend.core.Exceptions;

import com.example.lazyco.backend.core.Messages.MessageCodes;
import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends ExceptionWrapper {

  public EntityNotFoundException(MessageCodes code) {
    super(HttpStatus.NOT_FOUND, code);
  }

  public EntityNotFoundException(MessageCodes code, Object[] args) {
    super(HttpStatus.NOT_FOUND, code, args);
  }
}

package com.example.lazyco.backend.core.Exceptions;

import com.example.lazyco.backend.core.Messages.MessageCodes;
import org.springframework.http.HttpStatus;

public class EntityNotFoundExemption extends ExceptionWrapper {

  public EntityNotFoundExemption(MessageCodes code) {
    super(HttpStatus.NOT_FOUND, code);
  }

  public EntityNotFoundExemption(MessageCodes code, Object[] args) {
    super(HttpStatus.NOT_FOUND, code, args);
  }
}

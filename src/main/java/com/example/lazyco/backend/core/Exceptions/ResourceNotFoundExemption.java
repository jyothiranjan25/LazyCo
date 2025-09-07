package com.example.lazyco.backend.core.Exceptions;

import com.example.lazyco.backend.core.Messages.MessageCodes;
import org.springframework.http.HttpStatus;

public class ResourceNotFoundExemption extends ExceptionWrapper {

  public ResourceNotFoundExemption(MessageCodes code) {
    super(HttpStatus.NOT_FOUND, code);
  }

  public ResourceNotFoundExemption(MessageCodes code, Object[] args) {
    super(HttpStatus.NOT_FOUND, code, args);
  }
}

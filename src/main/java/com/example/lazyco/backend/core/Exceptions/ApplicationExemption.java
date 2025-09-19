package com.example.lazyco.backend.core.Exceptions;

import com.example.lazyco.backend.core.Messages.MessageCodes;
import org.springframework.http.HttpStatus;

public class ApplicationExemption extends ExceptionWrapper {

  public ApplicationExemption(MessageCodes code) {
    super(code);
  }

  public ApplicationExemption(MessageCodes code, Object[] args) {
    super(code, args);
  }

  public ApplicationExemption(HttpStatus httpStatus, MessageCodes code) {
    super(httpStatus, code);
  }

  public ApplicationExemption(HttpStatus httpStatus, MessageCodes code, Object[] args) {
    super(httpStatus, code, args);
  }
}

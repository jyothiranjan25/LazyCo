package com.example.lazyco.backend.core.Exceptions;

import com.example.lazyco.backend.core.Messages.MessageCodes;
import org.springframework.http.HttpStatus;

public class ApplicationException extends ExceptionWrapper {

  public ApplicationException(MessageCodes code) {
    super(code);
  }

  public ApplicationException(MessageCodes code, Object[] args) {
    super(code, args);
  }

  public ApplicationException(HttpStatus httpStatus, MessageCodes code) {
    super(httpStatus, code);
  }

  public ApplicationException(HttpStatus httpStatus, MessageCodes code, Object[] args) {
    super(httpStatus, code, args);
  }
}

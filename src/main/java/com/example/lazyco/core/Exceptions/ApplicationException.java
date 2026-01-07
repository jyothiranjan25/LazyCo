package com.example.lazyco.core.Exceptions;

import com.example.lazyco.core.Messages.MessageCodes;
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

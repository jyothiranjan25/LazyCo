package com.example.lazyco.backend.core.Exceptions;

import com.example.lazyco.backend.core.Messages.CustomMessage;
import com.example.lazyco.backend.core.Messages.MessageCodes;
import org.springframework.security.access.AccessDeniedException;

public class UnauthorizedAccessException extends AccessDeniedException {

  public UnauthorizedAccessException(String message) {
    super(message);
  }

  public UnauthorizedAccessException(MessageCodes code) {
    super(CustomMessage.getMessageString(code));
  }
}

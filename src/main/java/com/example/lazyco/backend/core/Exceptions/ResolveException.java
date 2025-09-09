package com.example.lazyco.backend.core.Exceptions;

import com.example.lazyco.backend.core.Messages.CustomMessage;
import jakarta.persistence.OptimisticLockException;
import org.hibernate.PropertyValueException;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.HttpRequestMethodNotSupportedException;

public class ResolveException {

  public static String resolveExceptionMessage(Throwable e) {
    return resolveException(e).getMessage();
  }

  public static SimpleResponseDTO resolveException(Throwable e) {
    SimpleResponseDTO simpleResponseDTO = new SimpleResponseDTO();

    while (e.getCause() != null) {
      e = e.getCause();
    }

    String defaultMessage = CustomMessage.getMessageString(CommonMessage.APPLICATION_ERROR);
    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    if (e instanceof PSQLException psqlEx) {
      String detail = psqlEx.getServerErrorMessage().getDetail();
      if (detail != null) {
        defaultMessage = detail.replaceFirst("Key \\([^)]*\\)=\\((.*?)\\)", "$1");
        status = HttpStatus.CONFLICT;
      }
    } else if (e instanceof PropertyValueException hibernateEx) {
      defaultMessage =
          CustomMessage.getMessageString(
              CommonMessage.FIELD_MISSING, hibernateEx.getPropertyName());
      status = HttpStatus.BAD_REQUEST;
    } else if (e instanceof OptimisticLockException
        || e instanceof ObjectOptimisticLockingFailureException) {
      defaultMessage = CustomMessage.getMessageString(CommonMessage.INTERNET_IS_SLOW);
      status = HttpStatus.CONFLICT;
    } else if (e instanceof HttpRequestMethodNotSupportedException httpEx) {
      defaultMessage = httpEx.getMessage();
      status = (HttpStatus) httpEx.getStatusCode();
    }

    simpleResponseDTO.setMessage(defaultMessage);
    simpleResponseDTO.setHttpStatus(status);
    return simpleResponseDTO;
  }
}

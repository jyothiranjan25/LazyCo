package com.example.lazyco.core.Exceptions;

import com.example.lazyco.core.Messages.CustomMessage;
import jakarta.persistence.OptimisticLockException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.hibernate.PropertyValueException;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.springframework.http.HttpStatus;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.servlet.NoHandlerFoundException;

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
      try {
        String sqlState = psqlEx.getSQLState();
        String detail = psqlEx.getServerErrorMessage().getDetail();
        if (PSQLState.UNIQUE_VIOLATION.getState().equals(sqlState)) {
          // Unique violation
          String value = null;
          if (detail != null) {
            // capture whatever is inside the parentheses after the equals sign
            Matcher m = Pattern.compile("=\\((.*?)\\)").matcher(detail);
            if (m.find()) {
              value = m.group(1);
            }
          }
          defaultMessage = CustomMessage.getMessageString(CommonMessage.DUPLICATE_FIELD, value);
          status = HttpStatus.CONFLICT;
        } else if (PSQLState.NOT_NULL_VIOLATION.getState().equals(sqlState)) {
          // NOT NULL violation
          String column = psqlEx.getServerErrorMessage().getColumn();
          defaultMessage = CustomMessage.getMessageString(CommonMessage.FIELD_MISSING, column);
          status = HttpStatus.BAD_REQUEST;
        } else if (PSQLState.FOREIGN_KEY_VIOLATION.getState().equals(sqlState)) {
          defaultMessage = CustomMessage.getMessageString(CommonMessage.REFERENCE_ERROR);
          status = HttpStatus.BAD_REQUEST;
        } else {
          defaultMessage = psqlEx.getMessage();
          status = HttpStatus.BAD_REQUEST;
        }
      } catch (Exception ignore) {
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
    } else if (e instanceof NoHandlerFoundException noHandlerEx) {
      defaultMessage = noHandlerEx.getMessage();
      status = HttpStatus.NOT_FOUND;
    }

    simpleResponseDTO.setMessage(defaultMessage);
    simpleResponseDTO.setHttpStatus(status);
    return simpleResponseDTO;
  }
}

package com.example.lazyco.core.Exceptions;

import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class BatchException extends ExceptionWrapper {

  public BatchException(AbstractDTO<?> dto) {
    super(dto);
  }

  public BatchException(HttpStatusCode httpStatusCode, AbstractDTO<?> dto) {
    super(httpStatusCode, dto);
  }

  public BatchException(HttpStatus status, AbstractDTO<?> dto) {
    super(status, dto);
  }
}

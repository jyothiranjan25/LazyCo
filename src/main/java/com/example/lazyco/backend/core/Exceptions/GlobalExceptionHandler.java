package com.example.lazyco.backend.core.Exceptions;

import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import com.example.lazyco.backend.core.Utils.ResponseUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  // handle all unexpected errors/ exceptions will be handled here
  @ExceptionHandler(ExceptionWrapper.class)
  public ResponseEntity<SimpleResponseDTO> applicationExceptionWrapper(ExceptionWrapper e) {
    ApplicationLogger.error(e);
    SimpleResponseDTO simpleResponseDTO = new SimpleResponseDTO();
    simpleResponseDTO.setMessage(e.getMessage());
    return ResponseUtils.sendResponse(e.getHttpStatus(), simpleResponseDTO);
  }

  // handle all unexpected errors/ exceptions will be handled here
  @ExceptionHandler(Throwable.class)
  public ResponseEntity<SimpleResponseDTO> handleUncheckedException(Throwable e) {
    ApplicationLogger.error(e);
    SimpleResponseDTO simpleResponseDTO = ResolveException.resolveException(e);
    HttpStatus httpStatus = simpleResponseDTO.getHttpStatus();
    simpleResponseDTO.setHttpStatus(null); // clear status to avoid redundancy
    return ResponseUtils.sendResponse(httpStatus, simpleResponseDTO);
  }
}

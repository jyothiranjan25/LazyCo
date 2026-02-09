package com.example.lazyco.core.Exceptions;

import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.Logger.ApplicationLogger;
import com.example.lazyco.core.Utils.ResponseUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  // handle all unexpected errors/ exceptions will be handled here
  @ExceptionHandler(ExceptionWrapper.class)
  public ResponseEntity<SimpleResponseDTO> applicationExceptionWrapper(ExceptionWrapper e) {
    Throwable cause = e;
    while (cause instanceof ExceptionWrapper && ((ExceptionWrapper) cause).getException() != null) {
      cause = ((ExceptionWrapper) cause).getException();
    }
    ApplicationLogger.error(cause);
    SimpleResponseDTO simpleResponseDTO = new SimpleResponseDTO();
    simpleResponseDTO.setMessage(e.getMessage());
    return ResponseUtils.sendResponse(e.getHttpStatus(), simpleResponseDTO);
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @ExceptionHandler(BatchException.class)
  public ResponseEntity<?> applicationExceptionWrapper(BatchException e) {
    AbstractDTO<?> failedDTO = e.getAbstractDTO();
    return ResponseUtils.sendResponse(e.getHttpStatus(), (AbstractDTO) (failedDTO));
  }

  // handle all unexpected errors/ exceptions will be handled here
  @ExceptionHandler(Throwable.class)
  public ResponseEntity<SimpleResponseDTO> handleUncheckedException(Throwable e) {
    ApplicationLogger.error(e);
    SimpleResponseDTO simpleResponseDTO = ResolveException.resolveException(e);
    HttpStatus httpStatus = simpleResponseDTO.getHttpStatus();
    simpleResponseDTO.setHttpStatus(null);
    return ResponseUtils.sendResponse(httpStatus, simpleResponseDTO);
  }
}

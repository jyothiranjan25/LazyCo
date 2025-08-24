package com.example.lazyco.backend.core.Exceptions;

import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // handle all unexpected errors/ exceptions will be handled here
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<SimpleResponseDTO> handleUncheckedException(Throwable e) {
        ApplicationLogger.error(e, e.getClass());
        SimpleResponseDTO simpleResponseDTO = new SimpleResponseDTO();
        simpleResponseDTO.setMessage(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(simpleResponseDTO);
    }
}

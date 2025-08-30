package com.example.lazyco.backend.core.Utils;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * Utility class for creating standardized HTTP responses Supports various response types including
 * DTOs, Files, JSONObject, etc.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseUtils {

  public static ResponseEntity<?> sendResponse(int code, String message) {
    return ResponseEntity.status(code).body(message);
  }

  public static <T extends AbstractDTO<T>> ResponseEntity<T> sendResponse(T abstractDTO) {
    return sendResponse(HttpStatus.OK, abstractDTO);
  }

  public static <T extends AbstractDTO<T>> ResponseEntity<T> sendResponse(
      HttpStatus httpStatus, T abstractDTO) {
    return ResponseEntity.status(httpStatus)
        .contentType(MediaType.APPLICATION_JSON)
        .body(abstractDTO);
  }

  public static <T extends AbstractDTO<T>> ResponseEntity<T> sendResponse(int code, T abstractDTO) {
    return ResponseEntity.status(code).contentType(MediaType.APPLICATION_JSON).body(abstractDTO);
  }
}

package com.example.lazyco.backend.core.Utils;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * Utility class for creating standardized HTTP responses Supports various response types including
 * DTOs, Files, JSONObject, etc.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseUtils {

  public static ResponseEntity<?> sendResponse(String message) {
    return sendResponse(HttpStatus.OK, message);
  }

  public static ResponseEntity<?> sendResponse(HttpStatus httpStatus, String message) {
    return sendResponse((HttpStatusCode) httpStatus, message);
  }

  public static ResponseEntity<?> sendResponse(HttpStatusCode httpStatusCode, String message) {
    return ResponseEntity.status(httpStatusCode).body(message);
  }

  public static <T extends AbstractDTO<T>> ResponseEntity<T> sendResponse(T abstractDTO) {
    return sendResponse(HttpStatus.OK, abstractDTO);
  }

  public static <T extends AbstractDTO<T>> ResponseEntity<T> sendResponse(
      HttpStatus httpStatus, T abstractDTO) {
    return sendResponse((HttpStatusCode) httpStatus, abstractDTO);
  }

  public static <T extends AbstractDTO<T>> ResponseEntity<T> sendResponse(
      HttpStatusCode httpStatusCode, T abstractDTO) {
    return ResponseEntity.status(httpStatusCode)
        .contentType(MediaType.APPLICATION_JSON)
        .body(abstractDTO);
  }
}

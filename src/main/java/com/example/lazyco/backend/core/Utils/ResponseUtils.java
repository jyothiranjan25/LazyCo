package com.example.lazyco.backend.core.Utils;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * Utility class for creating standardized HTTP responses Supports various response types including
 * DTOs, Files, JSONObject, etc.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseUtils {

  public static <T extends AbstractDTO<T>> ResponseEntity<T> sendResponse(T abstractDTO) {
    return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(abstractDTO);
  }

  public static <T extends AbstractDTO<T>> ResponseEntity<T> sendResponseByCode(
      int code, T abstractDTO) {
    return ResponseEntity.status(code).contentType(MediaType.APPLICATION_JSON).body(abstractDTO);
  }
}

package com.example.lazyco.backend.core.Utils;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.Exceptions.SimpleResponseDTO;
import com.example.lazyco.backend.core.File.FileDTO;
import java.io.ByteArrayOutputStream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.*;

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
    SimpleResponseDTO simpleResponseDTO = new SimpleResponseDTO();
    simpleResponseDTO.setMessage(message);
    return ResponseEntity.status(httpStatusCode)
        .contentType(MediaType.APPLICATION_JSON)
        .body(simpleResponseDTO);
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

  public static ResponseEntity<?> sendResponse(FileDTO fileDTO) {
    return sendFile(
        fileDTO.getByteArrayOutputStream(), fileDTO.getFullFileName(), fileDTO.getContentType());
  }

  public static ResponseEntity<?> sendFile(
      ByteArrayOutputStream fileStream, String fileName, String contentType) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.parseMediaType(contentType));
    headers.add("Content-Disposition", "attachment; filename=" + fileName);
    return new ResponseEntity<>(fileStream.toByteArray(), headers, HttpStatus.OK);
  }
}

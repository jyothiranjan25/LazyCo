package com.example.lazyco.core.Utils;

import static org.apache.commons.lang3.StringUtils.isNumeric;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public enum FieldTypeEnum {
  // Text Inputs
  TEXT,

  // Numeric Inputs
  NUMBER,

  // Date Inputs
  DATE,
  DATETIME,

  // Choice Inputs
  SELECT,
  MULTI_SELECT,

  // Contact Inputs
  EMAIL,
  PHONE,
  MOBILE,
  ;

  // Validation for specific formats
  public boolean validateField(Object value) {
    return switch (this) {
      case TEXT, SELECT -> value instanceof String;
      case NUMBER -> value instanceof Number || (value instanceof String str && isNumeric(str));
      case DATE, DATETIME -> value instanceof java.util.Date
          || value instanceof Instant
          || value instanceof LocalDate
          || value instanceof LocalDateTime
          || (value instanceof String str && isValidDate(str));
      case MULTI_SELECT -> value instanceof List<?>;
      case EMAIL -> value instanceof String && ((String) value).matches("^[A-Za-z0-9+_.-]+@(.+)$");
      case PHONE -> value instanceof String && ((String) value).matches("^\\+?[0-9]{7,15}$");
      case MOBILE -> value instanceof String && ((String) value).matches("^\\+?[0-9]{10,15}$");
      default -> false;
    };
  }

  private boolean isValidDate(String str) {
    try {
      // Handles ISO format like 2026-04-13T18:30:00.000Z
      java.time.Instant.parse(str);
      return true;
    } catch (Exception e1) {
      try {
        // Handles yyyy-MM-dd
        java.time.LocalDate.parse(str);
        return true;
      } catch (Exception e2) {
        try {
          // Handles yyyy-MM-ddTHH:mm:ss
          java.time.LocalDateTime.parse(str);
          return true;
        } catch (Exception e3) {
          return false;
        }
      }
    }
  }
}

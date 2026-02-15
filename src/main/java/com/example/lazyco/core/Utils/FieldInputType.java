package com.example.lazyco.core.Utils;

import java.util.Date;
import java.util.List;

public enum FieldInputType {
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
      case NUMBER -> value instanceof Number;
      case DATE, DATETIME -> value instanceof Date;
      case MULTI_SELECT -> value instanceof List<?>;
      case EMAIL -> value instanceof String && ((String) value).matches("^[A-Za-z0-9+_.-]+@(.+)$");
      case PHONE -> value instanceof String && ((String) value).matches("^\\+?[0-9]{7,15}$");
      case MOBILE -> value instanceof String && ((String) value).matches("^\\+?[0-9]{10,15}$");
      default -> false;
    };
  }
}

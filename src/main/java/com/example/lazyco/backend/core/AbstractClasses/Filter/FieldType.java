package com.example.lazyco.backend.core.AbstractClasses.Filter;

import java.time.LocalDate;
import java.time.LocalDateTime;

public enum FieldType {
  STRING,
  NUMBER,
  DATE,
  BOOLEAN,
  ENUM,
  MULTISELECT,
  UNKNOWN;

  public static FieldType fromClass(Class<?> type) {
    if (String.class.equals(type)) return STRING;
    if (Number.class.isAssignableFrom(type)
        || (type.isPrimitive()
            && (type == int.class
                || type == long.class
                || type == double.class
                || type == float.class))) {
      return NUMBER;
    }
    if (Boolean.class.equals(type) || type == boolean.class) return BOOLEAN;
    if (java.util.Date.class.isAssignableFrom(type)
        || LocalDate.class.equals(type)
        || LocalDateTime.class.equals(type)) {
      return DATE;
    }
    if (java.util.Collection.class.isAssignableFrom(type)) return MULTISELECT;
    if (type.isEnum()) return ENUM;
    return UNKNOWN;
  }
}

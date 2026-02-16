package com.example.lazyco.core.Utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class FieldParse {

  public static Long parseLong(Object value) {

    if (value == null) return null;

    if (value instanceof Long l) {
      return l;
    }

    if (value instanceof Integer i) {
      return i.longValue();
    }

    if (value instanceof Double d) {
      return d.longValue(); // 51.0 → 51
    }

    if (value instanceof String s) {
      if (s.contains(".")) {
        return Double.valueOf(s).longValue();
      }
      return Long.valueOf(s);
    }

    throw new IllegalArgumentException("Cannot convert to Long: " + value);
  }

  public static Integer parseInteger(Object value) {
    if (value == null) return null;
    if (value instanceof Integer i) {
      return i;
    }
    if (value instanceof Long l) {
      return l.intValue();
    }
    if (value instanceof Double d) {
      return d.intValue(); // 51.0 → 51
    }
    if (value instanceof String s) {
      if (s.contains(".")) {
        return Double.valueOf(s).intValue();
      }
      return Integer.valueOf(s);
    }
    throw new IllegalArgumentException("Cannot convert to Integer: " + value);
  }

  public static Double parseDouble(Object value) {
    if (value == null) return null;
    if (value instanceof Double d) {
      return d;
    }
    if (value instanceof Long l) {
      return l.doubleValue();
    }
    if (value instanceof Integer i) {
      return i.doubleValue();
    }
    if (value instanceof String s) {
      return Double.valueOf(s);
    }
    throw new IllegalArgumentException("Cannot convert to Double: " + value);
  }

  public static String parseString(Object value) {
    if (value == null) return null;
    if (value instanceof String s) {
      return s;
    }

    if (!(value instanceof Collection<?> collection) && !(value instanceof Map<?, ?>)) {
      throw new IllegalArgumentException("Cannot convert to String: " + value);
    }

    return value.toString();
  }

  public static String parseString(Object value, String defaultValue) {
    if (value == null) return defaultValue;
    if (value instanceof String s) {
      return s;
    }
    if (!(value instanceof Collection<?> collection) && !(value instanceof Map<?, ?>)) {
      return defaultValue;
    }
    return value.toString();
  }

  public static LocalDate parseLocalDate(Object value) {
    if (value == null) return null;
    if (value instanceof LocalDate ld) {
      return ld;
    }
    if (value instanceof String s) {
      DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
      DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
      try {
        return LocalDate.parse(s, DATE_FORMATTER);
      } catch (Exception e) {
        return LocalDate.parse(s, DATE_TIME_FORMATTER);
      }
    }
    throw new IllegalArgumentException("Cannot convert to LocalDate: " + value);
  }

  public static LocalDate parseLocalDate(Object value, String formatter) {
    if (value == null) return null;
    if (value instanceof LocalDate ld) {
      return ld;
    }
    if (value instanceof String s) {
      DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(formatter);
      return LocalDate.parse(s, dateFormatter);
    }
    throw new IllegalArgumentException("Cannot convert to LocalDate: " + value);
  }

  public static LocalTime parseLocalTime(Object value) {
    if (value == null) return null;
    if (value instanceof LocalTime lt) {
      return lt;
    }
    if (value instanceof String s) {
      DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
      DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
      try {
        return LocalTime.parse(s, DATE_TIME_FORMATTER);
      } catch (Exception e) {
        return LocalTime.parse(s, DATE_FORMATTER);
      }
    }
    throw new IllegalArgumentException("Cannot convert to LocalTime: " + value);
  }

  public static LocalTime parseLocalTime(Object value, String formater) {
    if (value == null) return null;
    if (value instanceof LocalTime lt) {
      return lt;
    }
    if (value instanceof String s) {
      DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(formater);
      return LocalTime.parse(s, dateTimeFormatter);
    }
    throw new IllegalArgumentException("Cannot convert to String: " + value);
  }

  public static LocalDateTime parseLocalDateTime(Object value) {
    if (value == null) return null;
    if (value instanceof LocalDateTime ldt) {
      return ldt;
    }
    if (value instanceof String s) {
      DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
      DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
      try {
        return LocalDateTime.parse(s, DATE_TIME_FORMATTER);
      } catch (Exception e) {
        LocalDate date = LocalDate.parse(s, DATE_FORMATTER);
        return date.atStartOfDay();
      }
    }
    throw new IllegalArgumentException("Cannot convert to LocalDateTime: " + value);
  }

  public static LocalDateTime parseLocalDateTime(Object value, String formatter) {
    if (value == null) return null;
    if (value instanceof LocalDateTime ldt) {
      return ldt;
    }
    if (value instanceof String s) {
      DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(formatter);
      return LocalDateTime.parse(s, dateTimeFormatter);
    }
    throw new IllegalArgumentException("Cannot convert to LocalDateTime: " + value);
  }

  public static Boolean parseBoolean(Object value) {
    if (value == null) return null;
    if (value instanceof Boolean b) {
      return b;
    }
    if (value instanceof String s) {
      return Boolean.valueOf(s);
    }
    throw new IllegalArgumentException("Cannot convert to Boolean: " + value);
  }

  public static Boolean parseBoolean(Object value, String formater) {
    if (value == null) return null;
    if (value instanceof Boolean b) {
      return b;
    }
    if (value instanceof String s) {
      return s.equalsIgnoreCase(formater);
    }
    throw new IllegalArgumentException("Cannot convert to Boolean: " + value);
  }

  public static <T extends Enum<T>> T parseEnum(Object value, Class<T> enumType) {
    if (value == null) return null;
    if (value instanceof String s) {
      String normalized = s.toUpperCase().trim();
      return Enum.valueOf(enumType, normalized);
    }
    throw new IllegalArgumentException(
        "Cannot convert to " + enumType.getSimpleName() + ": " + value);
  }

  public static List<?> parseList(Object value) {
    if (value == null) return null;
    if (value instanceof List<?> list) {
      return list;
    }
    throw new IllegalArgumentException("Cannot convert to List: " + value);
  }
}

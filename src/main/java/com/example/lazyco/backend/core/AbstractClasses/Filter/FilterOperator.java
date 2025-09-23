package com.example.lazyco.backend.core.AbstractClasses.Filter;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;
import lombok.Getter;

@Getter
public enum FilterOperator {
  EQUALS("EQUALS", "Equals"),
  NOT_EQUALS("NOT_EQUALS", "Not Equals"),
  CONTAINS("CONTAINS", "Contains"),
  NOT_CONTAINS("NOT_CONTAINS", "Does Not Contain"),
  STARTS_WITH("STARTS_WITH", "Starts With"),
  ENDS_WITH("ENDS_WITH", "Ends With"),
  GREATER_THAN("GREATER_THAN", "Greater Than"),
  GREATER_THAN_OR_EQUAL("GREATER_THAN_OR_EQUAL", "Greater Than or Equal"),
  LESS_THAN("LESS_THAN", "Less Than"),
  LESS_THAN_OR_EQUAL("LESS_THAN_OR_EQUAL", "Less Than or Equal"),
  BETWEEN("BETWEEN", "Between"),
  IN("IN", "In List"),
  NOT_IN("NOT_IN", "Not In List"),
  IS_NULL("IS_NULL", "Is Empty"),
  IS_NOT_NULL("IS_NOT_NULL", "Is Not Empty"),
  DATE_EQUALS("DATE_EQUALS", "Date Equals"),
  DATE_BEFORE("DATE_BEFORE", "Before Date"),
  DATE_AFTER("DATE_AFTER", "After Date"),
  DATE_BETWEEN("DATE_BETWEEN", "Date Between");
  ;
  private final String operatorName;
  private final String displayName;

  FilterOperator(String operatorName, String displayName) {
    this.operatorName = operatorName;
    this.displayName = displayName;
  }

  /** Get default operators for a given field type */
  public static FilterOperator[] getDefaultOperatorsForType(String fieldType) {
    return switch (fieldType.toLowerCase()) {
      case "string" -> new FilterOperator[] {
        EQUALS, CONTAINS, STARTS_WITH, ENDS_WITH, IS_NULL, IS_NOT_NULL
      };
      case "number", "integer", "double", "float", "long" -> new FilterOperator[] {
        EQUALS,
        GREATER_THAN,
        LESS_THAN,
        GREATER_THAN_OR_EQUAL,
        LESS_THAN_OR_EQUAL,
        BETWEEN,
        IS_NULL,
        IS_NOT_NULL
      };
      case "date", "datetime" -> new FilterOperator[] {
        DATE_EQUALS, DATE_BEFORE, DATE_AFTER, DATE_BETWEEN, IS_NULL, IS_NOT_NULL
      };
      case "boolean" -> new FilterOperator[] {EQUALS, IS_NULL, IS_NOT_NULL};
      case "enum" -> new FilterOperator[] {EQUALS, NOT_EQUALS, IN, NOT_IN, IS_NULL, IS_NOT_NULL};
      case "multiselect" -> new FilterOperator[] {IN, NOT_IN, CONTAINS, IS_NULL, IS_NOT_NULL};
      default -> new FilterOperator[] {EQUALS, CONTAINS};
    };
  }

  public record OperatorInfo(String name, String value) {}

  public static OperatorInfo[] getDefaultOperatorInfosForType(String fieldType) {
    FilterOperator[] ops = getDefaultOperatorsForType(fieldType);
    OperatorInfo[] infos = new OperatorInfo[ops.length];
    for (int i = 0; i < ops.length; i++) {
      infos[i] = new OperatorInfo(ops[i].getOperatorName(), ops[i].getDisplayName());
    }
    return infos;
  }

  public static String getFieldType(Field field) {
    Class<?> type = field.getType();
    if (type == String.class) {
      return "string";
    } else if (type == Integer.class
        || type == int.class
        || type == Long.class
        || type == long.class
        || type == Double.class
        || type == double.class
        || type == Float.class
        || type == float.class) {
      return "number";
    } else if (type == Boolean.class || type == boolean.class) {
      return "boolean";
    } else if (type == Date.class || type == LocalDate.class || type == LocalDateTime.class) {
      return "date";
    } else if (type == List.class || type == Set.class) {
      return "multiselect";
    } else if (type.isEnum()) {
      return "enum";
    } else {
      return "string";
    }
  }

  public enum MatchMode {
    START,
    END,
    ANYWHERE;

    public String resolveString(String string) {
      char w = '%';
      switch (this) {
        case START -> {
          return string + w;
        }
        case END -> {
          return w + string;
        }
        case ANYWHERE -> {
          return w + string + w;
        }
      }
      return string;
    }
  }
}

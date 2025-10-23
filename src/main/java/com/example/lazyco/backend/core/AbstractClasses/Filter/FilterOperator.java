package com.example.lazyco.backend.core.AbstractClasses.Filter;

import static com.example.lazyco.backend.core.AbstractClasses.Filter.FieldType.*;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
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
  DATE_BETWEEN("DATE_BETWEEN", "Date Between"),
  ;
  private final String operatorName;
  private final String displayName;

  FilterOperator(String operatorName, String displayName) {
    this.operatorName = operatorName;
    this.displayName = displayName;
  }

  /** Get default operators for a given field type */
  public static FilterOperator[] getDefaultOperatorsForType(FieldType type) {
    return switch (type) {
      case STRING -> new FilterOperator[] {
        EQUALS, CONTAINS, STARTS_WITH, ENDS_WITH, IS_NULL, IS_NOT_NULL
      };
      case NUMBER -> new FilterOperator[] {
        EQUALS,
        GREATER_THAN,
        LESS_THAN,
        GREATER_THAN_OR_EQUAL,
        LESS_THAN_OR_EQUAL,
        BETWEEN,
        IS_NULL,
        IS_NOT_NULL
      };
      case DATE -> new FilterOperator[] {
        DATE_EQUALS, DATE_BEFORE, DATE_AFTER, DATE_BETWEEN, IS_NULL, IS_NOT_NULL
      };
      case BOOLEAN -> new FilterOperator[] {EQUALS, IS_NULL, IS_NOT_NULL};
      case ENUM -> new FilterOperator[] {EQUALS, NOT_EQUALS, IN, NOT_IN, IS_NULL, IS_NOT_NULL};
      case MULTISELECT -> new FilterOperator[] {IN, NOT_IN, IS_NULL, IS_NOT_NULL};
      default -> new FilterOperator[] {EQUALS, CONTAINS};
    };
  }

  public static FieldType getFieldType(Field field) {
    return FieldType.fromClass(field.getType());
  }

  public static Class<?> getCollectionElementClass(Field field) {
    Class<?> rawType = field.getType();

    if (rawType.isArray()) {
      Class<?> component = rawType.getComponentType();
      return component.isEnum() ? Enum.class : component;
    }

    if (!Collection.class.isAssignableFrom(rawType)) {
      return rawType.isEnum() ? Enum.class : rawType;
    }

    Type gType = field.getGenericType();
    if (gType instanceof ParameterizedType pType) {
      Type[] args = pType.getActualTypeArguments();
      if (args.length == 1) {
        Type arg = args[0];
        if (arg instanceof Class<?> cls) {
          return cls.isEnum() ? Enum.class : cls;
        }
        if (arg instanceof ParameterizedType pt && pt.getRawType() instanceof Class<?> raw) {
          return raw.isEnum() ? Enum.class : raw;
        }
      }
    }

    // Fallback when generic info is erased
    return Object.class;
  }

  public static Class<?> getEnumElementClass(Field field) {
    Class<?> rawType = field.getType();

    // Arrays
    if (rawType.isArray()) {
      return rawType.getComponentType();
    }

    // Collections
    if (Collection.class.isAssignableFrom(rawType)) {
      Type gType = field.getGenericType();
      if (gType instanceof ParameterizedType pType) {
        Type[] args = pType.getActualTypeArguments();
        if (args.length == 1) {
          Type arg = args[0];
          if (arg instanceof Class<?> cls && cls.isEnum()) {
            return cls;
          }
          if (arg instanceof ParameterizedType pt
              && pt.getRawType() instanceof Class<?> raw
              && raw.isEnum()) {
            return raw;
          }
        }
      }
      return null; // collection with no enum element
    }

    // Single enum field
    return rawType;
  }

  public enum MatchMode {
    START,
    END,
    ANYWHERE;

    public static Object resolveString(MatchMode mode, Object s) {
      return switch (mode) {
        case START -> s + "%";
        case END -> "%" + s;
        case ANYWHERE -> "%" + s + "%";
      };
    }
  }
}

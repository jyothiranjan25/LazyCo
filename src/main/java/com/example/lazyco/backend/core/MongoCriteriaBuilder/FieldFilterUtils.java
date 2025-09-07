package com.example.lazyco.backend.core.MongoCriteriaBuilder;

import com.example.lazyco.backend.core.AbstractDocClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import com.google.gson.annotations.SerializedName;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class FieldFilterUtils {

  public static void addInternalFieldFilters(MongoCriteriaBuilderWrapper criteriaWrapper) {
    Class<?> filterClass = criteriaWrapper.getFilter().getClass();
    for (Field field : getAllFields(filterClass)) {
      addSingleFieldFilter(criteriaWrapper, field);
    }
  }

  private static void addSingleFieldFilter(MongoCriteriaBuilderWrapper criteriaWrapper, Field field) {
    if (!field.isAnnotationPresent(InternalFilterableField.class)) {
      return;
    }
    try {
      field.setAccessible(true);
      Object value = field.get(criteriaWrapper.getFilter());

      // skip null or empty collections
      if (value == null || (value instanceof Collection && ((Collection<?>) value).isEmpty())) {
        return;
      }

      String fieldName = getSerializedFieldName(field);

      // check operator
      String operatorFieldName = field.getName() + "Operator";
      String operator = getOperatorValue(criteriaWrapper.getFilter(), operatorFieldName);

      if (value instanceof Collection<?>) {
        criteriaWrapper.in(fieldName, (Collection<?>) value);
      } else {
        applyFilterOperation(criteriaWrapper, fieldName, value, operator);
      }

    } catch (IllegalAccessException e) {
      ApplicationLogger.error("Failed to access field " + field.getName(), e);
    }
  }

  private static void applyFilterOperation(
      MongoCriteriaBuilderWrapper criteriaWrapper,
      String fieldName,
      Object value,
      String operator) {
    if (operator == null) {
      criteriaWrapper.eq(fieldName, value);
      return;
    }

    switch (operator.toLowerCase()) {
      case "contains":
        criteriaWrapper.like(fieldName, "%" + value + "%");
        break;
      case "startswith":
        criteriaWrapper.like(fieldName, value + "%");
        break;
      case "endswith":
        criteriaWrapper.like(fieldName, "%" + value);
        break;
      case "greaterthan":
        criteriaWrapper.gt(fieldName, value);
        break;
      case "greaterthanorequal":
        criteriaWrapper.ge(fieldName, value);
        break;
      case "lessthan":
        criteriaWrapper.lt(fieldName, value);
        break;
      case "lessthanorequal":
        criteriaWrapper.le(fieldName, value);
        break;
      case "notequals":
        criteriaWrapper.notEqual(fieldName, value);
        break;
      default:
        criteriaWrapper.eq(fieldName, value);
        break;
    }
  }

  private static String getOperatorValue(AbstractDTO<?> filter, String operatorFieldName) {
    try {
      Field operatorField = findFieldByName(filter.getClass(), operatorFieldName);
      if (operatorField != null) {
        operatorField.setAccessible(true);
        Object val = operatorField.get(filter);
        if (val != null) return val.toString();
      }

      // fallback: snake_case serialized name
      String snakeCase = camelToSnakeCase(operatorFieldName);
      operatorField = findFieldBySerializedName(filter.getClass(), snakeCase);
      if (operatorField != null) {
        operatorField.setAccessible(true);
        Object val = operatorField.get(filter);
        if (val != null) return val.toString();
      }
    } catch (Exception ignored) {
    }
    return null;
  }

  private static String camelToSnakeCase(String camelCase) {
    return camelCase.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
  }

  private static Field findFieldByName(Class<?> clazz, String fieldName) {
    while (clazz != null && clazz != Object.class) {
      try {
        return clazz.getDeclaredField(fieldName);
      } catch (NoSuchFieldException e) {
        clazz = clazz.getSuperclass();
      }
    }
    return null;
  }

  private static Field findFieldBySerializedName(Class<?> clazz, String serializedName) {
    while (clazz != null && clazz != Object.class) {
      for (Field field : clazz.getDeclaredFields()) {
        SerializedName annotation = field.getAnnotation(SerializedName.class);
        if (annotation != null && annotation.value().equals(serializedName)) {
          return field;
        }
      }
      clazz = clazz.getSuperclass();
    }
    return null;
  }

  private static String getSerializedFieldName(Field field) {
    SerializedName annotation = field.getAnnotation(SerializedName.class);
    return (annotation != null) ? annotation.value() : field.getName();
  }

  private static List<Field> getAllFields(Class<?> clazz) {
    List<Field> fields = new ArrayList<>(Arrays.asList(clazz.getDeclaredFields()));
    if (clazz.getSuperclass() != null) {
      fields.addAll(getAllFields(clazz.getSuperclass()));
    }
    return fields;
  }
}

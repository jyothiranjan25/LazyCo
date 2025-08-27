package com.example.lazyco.backend.core.CriteriaBuilder.FieldFiltering;

import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.CriteriaBuilder.CriteriaBuilderWrapper;
import com.google.gson.annotations.SerializedName;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FieldFilterUtils {

  private static void addSingleFieldFilter(
      CriteriaBuilderWrapper criteriaBuilderWrapper, Field field) {
    if (!field.isAnnotationPresent(InternalFilterableField.class)) {
      return;
    }
    try {
      field.setAccessible(true);

      Object value = field.get(criteriaBuilderWrapper.getFilter());
      // if value is absent, skip that field
      if (value == null || (value instanceof Collection && ((Collection<?>) value).isEmpty())) {
        return;
      }

      Path<?> fieldPathNode = getPathNode(criteriaBuilderWrapper, field);

      // Check for corresponding operator field
      String operatorFieldName = field.getName() + "Operator";
      String operator = getOperatorValue(criteriaBuilderWrapper.getFilter(), operatorFieldName);

      if (value instanceof Collection<?>) {
        criteriaBuilderWrapper.in(fieldPathNode, (Collection<?>) value);
      } else {
        // Apply the appropriate operation based on operator
        applyFilterOperation(
            criteriaBuilderWrapper, fieldPathNode, value, operator, field.getName());
      }
    } catch (Throwable t) {
      // Ignore - field may not be accessible or other reflection issues
      t.printStackTrace();
    }
  }

  private static String getOperatorValue(AbstractDTO<?> filter, String operatorFieldName) {
    try {
      // First try with the Java field name
      Field operatorField = findFieldByName(filter.getClass(), operatorFieldName);
      if (operatorField != null) {
        operatorField.setAccessible(true);
        Object operatorValue = operatorField.get(filter);
        if (operatorValue != null) {
          return operatorValue.toString();
        }
      }

      // If not found, try with underscore naming convention for serialized names
      // Convert camelCase to snake_case (e.g., startDateOperator -> start_date_operator)
      String snakeCaseOperatorFieldName = camelToSnakeCase(operatorFieldName);
      operatorField = findFieldBySerializedName(filter.getClass(), snakeCaseOperatorFieldName);
      if (operatorField != null) {
        operatorField.setAccessible(true);
        Object operatorValue = operatorField.get(filter);
        if (operatorValue != null) {
          return operatorValue.toString();
        }
      }
    } catch (Exception e) {
      // Ignore - operator field may not exist or may not be accessible
    }
    return null;
  }

  private static String camelToSnakeCase(String camelCase) {
    return camelCase.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
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

  private static void applyFilterOperation(
      CriteriaBuilderWrapper criteriaBuilderWrapper,
      Path<?> fieldPath,
      Object value,
      String operator,
      String fieldName) {
    if (operator == null) {
      // Default to equals if no operator specified
      criteriaBuilderWrapper.eq(fieldPath, value);
      return;
    }

    switch (operator.toLowerCase()) {
      case "contains":
        // Use the field name directly - CriteriaBuilderWrapper will resolve the path
        criteriaBuilderWrapper.like(fieldName, "%" + value.toString() + "%");
        break;
      case "startswith":
        criteriaBuilderWrapper.like(fieldName, value.toString() + "%");
        break;
      case "endswith":
        criteriaBuilderWrapper.like(fieldName, "%" + value.toString());
        break;
      case "greaterthan":
        criteriaBuilderWrapper.gt(fieldName, value);
        break;
      case "greaterthanorequal":
        criteriaBuilderWrapper.ge(fieldName, value);
        break;
      case "lessthan":
        criteriaBuilderWrapper.lt(fieldName, value);
        break;
      case "lessthanorequal":
        criteriaBuilderWrapper.le(fieldName, value);
        break;
      case "notequals":
        criteriaBuilderWrapper.notEqual(fieldName, value);
        break;
      default:
        // Default to equals for unknown operators
        criteriaBuilderWrapper.eq(fieldPath, value);
        break;
    }
  }

  public static void addInternalFieldFilters(CriteriaBuilderWrapper criteriaBuilderWrapper) {
    Class<?> filterClass = criteriaBuilderWrapper.getFilter().getClass();
    for (Field field : getAllFields(filterClass)) {
      addSingleFieldFilter(criteriaBuilderWrapper, field);
    }
  }

  public static Path<?> getPathNode(
      CriteriaBuilderWrapper criteriaBuilderWrapper, String fullyQualifiedPath) {
    // Validate input
    if (fullyQualifiedPath == null || fullyQualifiedPath.trim().isEmpty()) {
      throw new IllegalArgumentException("Fully qualified path cannot be null or empty");
    }

    // Remove any trailing dots
    fullyQualifiedPath = fullyQualifiedPath.trim().replaceAll("\\.+$", "");

    if (fullyQualifiedPath.isEmpty()) {
      throw new IllegalArgumentException("Fully qualified path cannot be empty after trimming");
    }
    int fieldNameBeginIndex = fullyQualifiedPath.lastIndexOf('.') + 1;
    String fieldName = fullyQualifiedPath.substring(fieldNameBeginIndex);
    if (fieldNameBeginIndex == 0) {
      return criteriaBuilderWrapper.getRoot().get(fieldName);
    } else {
      Join<?, ?> subRoot =
          getSubRoot(
              criteriaBuilderWrapper, fullyQualifiedPath.substring(0, fieldNameBeginIndex - 1));
      return subRoot.get(fieldName);
    }
  }

  public static Join<?, ?> getSubRoot(
      CriteriaBuilderWrapper criteriaBuilderWrapper, String subRootPath) {
    if (criteriaBuilderWrapper.getFullyQualifiedPathToJoinMap().containsKey(subRootPath)) {
      return criteriaBuilderWrapper.getFullyQualifiedPathToJoinMap().get(subRootPath);
    }
    int lastJoinIndex = subRootPath.lastIndexOf('.') + 1;
    String lastJoin = subRootPath.substring(lastJoinIndex);
    Join<?, ?> subRoot;
    JoinType joinType =
        criteriaBuilderWrapper
            .getFullyQualifiedPathToJoinTypeMap()
            .getOrDefault(subRootPath, JoinType.LEFT);

    if (lastJoinIndex == 0) {
      subRoot = criteriaBuilderWrapper.getRoot().join(lastJoin, joinType);
    } else {
      Join<?, ?> parentSubRoot =
          getSubRoot(criteriaBuilderWrapper, subRootPath.substring(0, lastJoinIndex - 1));
      subRoot = parentSubRoot.join(lastJoin, joinType);
    }
    criteriaBuilderWrapper.getFullyQualifiedPathToJoinMap().put(subRootPath, subRoot);
    return subRoot;
  }

  public static Path<?> getPathNode(CriteriaBuilderWrapper criteriaBuilderWrapper, Field field) {
    // Validate field is not null
    if (field == null) {
      throw new IllegalArgumentException("Field cannot be null when resolving path node");
    }

    String aliasPath = "";
    String fullyQualifiedPath = "";

    // Get field name for fallback
    String fieldName = getSerializedFieldName(field);
    // Check for @FieldPath annotation
    InternalFilterableField fieldPath = field.getAnnotation(InternalFilterableField.class);
    if (fieldPath != null) {
      fullyQualifiedPath = fieldPath.fullyQualifiedPath();
      if (!fieldPath.aliasPath().isEmpty()) {
        aliasPath = fieldPath.aliasPath();
      }
    }

    if (!fullyQualifiedPath.isEmpty()) {
      return getPathNode(criteriaBuilderWrapper, fullyQualifiedPath);
    } else {
      // Try to resolve using alias path, but fall back to field name if it fails
      String resolvedPath = null;
      if (!aliasPath.isEmpty()) {
        resolvedPath = criteriaBuilderWrapper.getFullyQualifiedPath(aliasPath);
      }

      // If no valid path resolved, use the field name directly
      if (resolvedPath == null || resolvedPath.trim().isEmpty()) {
        resolvedPath = fieldName;
      }

      return getPathNode(criteriaBuilderWrapper, resolvedPath);
    }
  }

  /**
   * Gets the serialized name for a field, preferring @SerializedName annotation over the Java field
   * name
   */
  private static String getSerializedFieldName(Field field) {
    // Check for @SerializedName annotation first
    if (field.isAnnotationPresent(SerializedName.class)) {
      return field.getAnnotation(SerializedName.class).value();
    }

    // Fall back to Java field name
    return field.getName();
  }

  public static List<Field> getAllFields(Class<?> clazz) {
    List<Field> fields = new ArrayList<>();

    // Get fields from the current class
    for (Field field : clazz.getDeclaredFields()) {
      fields.add(field);
    }

    // Get fields from superclasses recursively
    if (clazz.getSuperclass() != null) {
      fields.addAll(getAllFields(clazz.getSuperclass()));
    }

    return fields;
  }

  public static Field getField(String serializedName, AbstractDTO<?> filter) {
    for (Field field : getAllFields(filter.getClass())) {
      if (getKeyNameForField(field).equals(serializedName)) {
        return field;
      }
    }
    return null;
  }

  public static Path<?> getPathNode(
      CriteriaBuilderWrapper criteriaBuilderWrapper, String serializedName, AbstractDTO filter) {
    Field field = getField(serializedName, filter);
    return getPathNode(criteriaBuilderWrapper, field);
  }

  /**
   * key is either the serializedName value(if present) or the fieldName itself (if serializedName
   * annotation not found.)
   */
  public static String getKeyNameForField(Field field) {
    String key;
    if (field.isAnnotationPresent(SerializedName.class)) {
      SerializedName serializedName = field.getAnnotation(SerializedName.class);
      key = serializedName.value();
    } else {
      key = field.getName();
    }
    return key;
  }
}

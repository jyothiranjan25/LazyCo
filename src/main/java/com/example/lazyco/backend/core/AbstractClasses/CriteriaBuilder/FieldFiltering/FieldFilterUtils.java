package com.example.lazyco.backend.core.AbstractClasses.CriteriaBuilder.FieldFiltering;

import com.example.lazyco.backend.core.AbstractClasses.CriteriaBuilder.CriteriaBuilderWrapper;
import com.example.lazyco.backend.core.AbstractClasses.Entity.AbstractModel;
import com.example.lazyco.backend.core.AbstractClasses.Entity.AbstractRBACModel;
import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class FieldFilterUtils {

  // Cache for reflection operations to improve performance
  private static final Map<Class<?>, List<Field>> fieldCache = new ConcurrentHashMap<>();
  private static final Map<Class<?>, List<String>> entityFieldCache = new ConcurrentHashMap<>();
  private static final Map<Class<?>, List<Path<?>>> dtoFieldCache = new ConcurrentHashMap<>();
  private static final Map<Class<?>, List<Field>> searchableFieldsCache = new ConcurrentHashMap<>();

  public static void addInternalFieldFilters(CriteriaBuilderWrapper criteriaBuilderWrapper) {
    Class<?> filterClass = criteriaBuilderWrapper.getFilter().getClass();
    List<Field> fields = getAllFields(filterClass);

    for (Field field : fields) {
      try {
        addSingleFieldFilter(criteriaBuilderWrapper, field);
      } catch (Exception e) {
        ApplicationLogger.error("Failed to add field filter for: " + field.getName(), e);
        // Continue processing other fields even if one fails
      }
    }
  }

  private static void addSingleFieldFilter(
      CriteriaBuilderWrapper criteriaBuilderWrapper, Field field) {
    if (!field.isAnnotationPresent(InternalFilterableField.class)) {
      return;
    }

    try {
      field.setAccessible(true);

      Object value = field.get(criteriaBuilderWrapper.getFilter());
      // if value is absent or empty collection, skip
      if (value == null || (value instanceof Collection && ((Collection<?>) value).isEmpty())) {
        return;
      }

      Path<?> fieldPathNode = getPathNode(criteriaBuilderWrapper, field);
      if (fieldPathNode == null) {
        ApplicationLogger.error("Failed to resolve path for field: " + field.getName());
        return;
      }

      if (value instanceof Collection<?>) {
        criteriaBuilderWrapper.in(fieldPathNode, (Collection<?>) value);
      } else {
        criteriaBuilderWrapper.eq(fieldPathNode, value);
      }
    } catch (Exception e) {
      ApplicationLogger.error("Error processing field filter for: " + field.getName(), e);
    }
  }

  public static Path<?> getPathNode(CriteriaBuilderWrapper criteriaBuilderWrapper, Field field) {
    // Validate field is not null
    if (field == null) {
      throw new IllegalArgumentException("Field cannot be null when resolving path node");
    }

    try {
      String aliasPath = "";
      String fullyQualifiedPath = "";

      // Get field name for fallback
      String fieldName = field.getName();

      // Check for @FieldPath annotation
      FieldPath fieldPath = field.getAnnotation(FieldPath.class);
      if (fieldPath != null) {
        fullyQualifiedPath = fieldPath.fullyQualifiedPath();
        if (!fieldPath.aliasPath().isEmpty()) {
          aliasPath = fieldPath.aliasPath();
        }
      }

      if (!fullyQualifiedPath.isEmpty()) {
        if (fieldPath.shouldFetch()) {
          // You actually want the related data
          criteriaBuilderWrapper.fetch(fullyQualifiedPath);
        } else {
          // Just create joins for filtering, don't fetch
          ensureJoinExists(criteriaBuilderWrapper, fullyQualifiedPath);
        }
        return getPathNode(criteriaBuilderWrapper, fullyQualifiedPath);
      } else {
        // Try to resolve using alias path, but fall back to field name if it fails
        String resolvedPath = null;
        if (!aliasPath.isEmpty()) {
          try {
            resolvedPath = criteriaBuilderWrapper.getFullyQualifiedPath(aliasPath);
          } catch (Exception e) {
            ApplicationLogger.error("Failed to resolve alias path: " + aliasPath, e);
          }
        }

        // If no valid path resolved, use the field name directly
        if (resolvedPath == null || resolvedPath.trim().isEmpty()) {
          resolvedPath = fieldName;
        }

        return getPathNode(criteriaBuilderWrapper, resolvedPath);
      }
    } catch (Exception e) {
      ApplicationLogger.error("Failed to get path node for field: " + field.getName(), e);
      return null;
    }
  }

  private static void ensureJoinExists(CriteriaBuilderWrapper wrapper, String path) {
    if (path == null || path.trim().isEmpty()) {
      return;
    }

    try {
      int lastDotIndex = path.lastIndexOf('.');
      if (lastDotIndex > 0) {
        String joinPath = path.substring(0, lastDotIndex);
        if (!wrapper.getFullyQualifiedPathToJoinMap().containsKey(joinPath)) {
          wrapper.join(joinPath, JoinType.LEFT);
        }
      }
    } catch (Exception e) {
      ApplicationLogger.error("Failed to ensure join exists for path: " + path, e);
    }
  }

  public static Path<?> getPathNode(
      CriteriaBuilderWrapper criteriaBuilderWrapper, String fullyQualifiedPath) {
    // Validate input
    if (fullyQualifiedPath == null || fullyQualifiedPath.trim().isEmpty()) {
      throw new IllegalArgumentException("Fully qualified path cannot be null or empty");
    }

    try {
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
        return subRoot != null ? subRoot.get(fieldName) : null;
      }
    } catch (Exception e) {
      ApplicationLogger.error("Failed to get path node for: " + fullyQualifiedPath, e);
      return null;
    }
  }

  public static Join<?, ?> getSubRoot(
      CriteriaBuilderWrapper criteriaBuilderWrapper, String subRootPath) {
    if (subRootPath == null || subRootPath.trim().isEmpty()) {
      return null;
    }

    try {
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
        if (parentSubRoot == null) {
          return null;
        }
        subRoot = parentSubRoot.join(lastJoin, joinType);
      }

      criteriaBuilderWrapper.getFullyQualifiedPathToJoinMap().put(subRootPath, subRoot);
      return subRoot;
    } catch (Exception e) {
      ApplicationLogger.error("Failed to get subroot for path: " + subRootPath, e);
      return null;
    }
  }

  public static List<Field> getAllFields(Class<?> clazz) {
    // Use cache to improve performance
    return fieldCache.computeIfAbsent(
        clazz,
        k -> {
          List<Field> fields = new ArrayList<>();
          Class<?> currentClass = k;

          while (currentClass != null && currentClass != Object.class) {
            try {
              fields.addAll(Arrays.asList(currentClass.getDeclaredFields()));
            } catch (Exception e) {
              ApplicationLogger.error(
                  "Failed to get fields for class: " + currentClass.getName(), e);
            }
            currentClass = currentClass.getSuperclass();
          }

          return fields;
        });
  }

  // Add search string filter
  public static void addSearchStringFilter(CriteriaBuilderWrapper criteriaBuilderWrapper) {

    String searchString = criteriaBuilderWrapper.getFilter().getSearchString();
    if (searchString == null || searchString.isBlank()) {
      return;
    }

    Class<?> filterableEntityClass = criteriaBuilderWrapper.getFilter().getFilterableEntityClass();

    // Collect searchable fields
    List<String> entityFieldNames = getSearchableEntityFieldNames(filterableEntityClass);
    List<Path<?>> dtoFieldPaths = getSearchableDtoFieldPaths(criteriaBuilderWrapper);

    List<Predicate> searchPredicates = new ArrayList<>();

    for (String fieldName : entityFieldNames) {
      searchPredicates.add(criteriaBuilderWrapper.getSearchCriteria(fieldName, searchString));
    }
    for (Path<?> path : dtoFieldPaths) {
      searchPredicates.add(criteriaBuilderWrapper.getSearchCriteria(path, searchString));
    }
    if (!searchPredicates.isEmpty()) {
      criteriaBuilderWrapper.or(searchPredicates.toArray(new Predicate[0]));
    }
  }

  private static List<String> getSearchableEntityFieldNames(Class<?> clazz) {
    return entityFieldCache.computeIfAbsent(
        clazz,
        cls -> {
          List<Field> fields = getAllSearchableFields(cls);
          return fields.stream()
              .filter(
                  f ->
                      !Collection.class.isAssignableFrom(f.getType())
                          && !Map.class.isAssignableFrom(f.getType()))
              .map(Field::getName)
              .distinct()
              .collect(Collectors.toList());
        });
  }

  private static List<Path<?>> getSearchableDtoFieldPaths(CriteriaBuilderWrapper cbw) {
    Class<?> dtoClass = cbw.getFilter().getClass();
    return dtoFieldCache.computeIfAbsent(
        dtoClass,
        cls -> {
          List<Field> fields = getAllSearchableFields(cls);
          List<Path<?>> paths = new ArrayList<>();
          for (Field field : fields) {
            if (field.isAnnotationPresent(FieldPath.class)) {
              paths.add(getPathNode(cbw, field));
            }
          }
          return paths;
        });
  }

  private static List<Field> getAllSearchableFields(Class<?> clazz) {
    return searchableFieldsCache.computeIfAbsent(
        clazz,
        k -> {
          List<Field> fields = new ArrayList<>();
          Class<?> current = k; // <-- use k here

          while (current != null && current != Object.class) {
            // Stop if the current class is AbstractModel or AbstractRBACModel
            if (current == AbstractModel.class || current == AbstractRBACModel.class) {
              break;
            }

            Collections.addAll(fields, current.getDeclaredFields());
            current = current.getSuperclass();
          }
          return fields;
        });
  }

  // Clear cache when needed (useful for testing or memory management)
  public static void clearCache() {
    fieldCache.clear();
  }
}

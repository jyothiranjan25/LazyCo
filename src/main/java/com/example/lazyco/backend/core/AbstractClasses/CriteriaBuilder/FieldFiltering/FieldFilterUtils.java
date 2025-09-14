package com.example.lazyco.backend.core.AbstractClasses.CriteriaBuilder.FieldFiltering;

import com.example.lazyco.backend.core.AbstractClasses.CriteriaBuilder.CriteriaBuilderWrapper;
import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import com.google.gson.annotations.SerializedName;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("rawtypes")
public class FieldFilterUtils {

    // Cache for reflection operations to improve performance
    private static final ConcurrentHashMap<Class<?>, List<Field>> fieldCache = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Field> fieldByNameCache = new ConcurrentHashMap<>();

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
            String fieldName = getKeyNameForField(field);

            // Check for @FieldPath annotation
            FieldPath fieldPath = field.getAnnotation(FieldPath.class);
            if (fieldPath != null) {
                fullyQualifiedPath = fieldPath.fullyQualifiedPath();
                if (!fieldPath.aliasPath().isEmpty()) {
                    aliasPath = fieldPath.aliasPath();
                }
            }

            if (!fullyQualifiedPath.isEmpty()) {
                if (fieldPath != null && fieldPath.shouldFetch()) {
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

    private static String getOperatorValue(AbstractDTO<?> filter, String operatorFieldName) {
        try {
            // Use cache for field lookup
            String cacheKey = filter.getClass().getName() + "." + operatorFieldName;
            Field operatorField = fieldByNameCache.computeIfAbsent(cacheKey, k -> {
                // First try with the Java field name
                Field field = findFieldByName(filter.getClass(), operatorFieldName);
                if (field != null) {
                    return field;
                }

                // If not found, try with underscore naming convention for serialized names
                String snakeCaseOperatorFieldName = camelToSnakeCase(operatorFieldName);
                return findFieldBySerializedName(filter.getClass(), snakeCaseOperatorFieldName);
            });

            if (operatorField != null) {
                operatorField.setAccessible(true);
                Object operatorValue = operatorField.get(filter);
                if (operatorValue != null) {
                    return operatorValue.toString();
                }
            }
        } catch (Exception e) {
            ApplicationLogger.error("Failed to get operator value for: " + operatorFieldName, e);
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

        if (value == null) {
            return;
        }

        try {
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
                case "isnull":
                    criteriaBuilderWrapper.isNull(fieldName);
                    break;
                case "isnotnull":
                    criteriaBuilderWrapper.isNotNull(fieldName);
                    break;
                default:
                    // Default to equals for unknown operators
                    criteriaBuilderWrapper.eq(fieldPath, value);
                    break;
            }
        } catch (Exception e) {
            ApplicationLogger.error("Failed to apply filter operation: " + operator + " for field: " + fieldName, e);
            // Fallback to simple equality if operation fails
            criteriaBuilderWrapper.eq(fieldPath, value);
        }
    }

    public static List<Field> getAllFields(Class<?> clazz) {
        // Use cache to improve performance
        return fieldCache.computeIfAbsent(clazz, k -> {
            List<Field> fields = new ArrayList<>();
            Class<?> currentClass = k;

            while (currentClass != null && currentClass != Object.class) {
                try {
                    fields.addAll(Arrays.asList(currentClass.getDeclaredFields()));
                } catch (Exception e) {
                    ApplicationLogger.error("Failed to get fields for class: " + currentClass.getName(), e);
                }
                currentClass = currentClass.getSuperclass();
            }

            return fields;
        });
    }

    public static Field getField(String serializedName, AbstractDTO<?> filter) {
        if (serializedName == null || filter == null) {
            return null;
        }

        try {
            String cacheKey = filter.getClass().getName() + "." + serializedName;
            return fieldByNameCache.computeIfAbsent(cacheKey, k -> {
                for (Field field : getAllFields(filter.getClass())) {
                    if (getKeyNameForField(field).equals(serializedName)) {
                        return field;
                    }
                }
                return null;
            });
        } catch (Exception e) {
            ApplicationLogger.error("Failed to get field by serialized name: " + serializedName, e);
            return null;
        }
    }

    public static Path<?> getPathNode(
            CriteriaBuilderWrapper criteriaBuilderWrapper, String serializedName, AbstractDTO filter) {
        Field field = getField(serializedName, filter);
        return field != null ? getPathNode(criteriaBuilderWrapper, field) : null;
    }

    private static String camelToSnakeCase(String camelCase) {
        if (camelCase == null || camelCase.isEmpty()) {
            return camelCase;
        }
        return camelCase.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }

    private static Field findFieldBySerializedName(Class<?> clazz, String serializedName) {
        while (clazz != null && clazz != Object.class) {
            try {
                for (Field field : clazz.getDeclaredFields()) {
                    SerializedName annotation = field.getAnnotation(SerializedName.class);
                    if (annotation != null && annotation.value().equals(serializedName)) {
                        return field;
                    }
                }
            } catch (Exception e) {
                ApplicationLogger.error("Error scanning fields in class: " + clazz.getName(), e);
            }
            clazz = clazz.getSuperclass();
        }
        return null;
    }

    /**
     * key is either the serializedName value(if present) or the fieldName itself (if serializedName
     * annotation not found.)
     */
    public static String getKeyNameForField(Field field) {
        if (field == null) {
            return null;
        }

        String key;
        if (field.isAnnotationPresent(SerializedName.class)) {
            SerializedName serializedName = field.getAnnotation(SerializedName.class);
            key = serializedName.value();
        } else {
            key = field.getName();
        }
        return key;
    }

    // Clear cache when needed (useful for testing or memory management)
    public static void clearCache() {
        fieldCache.clear();
        fieldByNameCache.clear();
    }
}
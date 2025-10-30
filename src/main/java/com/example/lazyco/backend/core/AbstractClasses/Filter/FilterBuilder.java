package com.example.lazyco.backend.core.AbstractClasses.Filter;

import com.example.lazyco.backend.core.AbstractClasses.CriteriaBuilder.CriteriaBuilderWrapper;
import com.example.lazyco.backend.core.AbstractClasses.CriteriaBuilder.FieldFiltering.FieldFilterUtils;
import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import java.lang.reflect.Field;
import java.util.*;

public class FilterBuilder {

  public static void build(CriteriaBuilderWrapper criteriaBuilderWrapper) {
    if (criteriaBuilderWrapper != null
        && criteriaBuilderWrapper.getFilter() != null
        && criteriaBuilderWrapper.getFilter().getFilterFieldMetadata() != null) {
      Class<?> dtoClass = criteriaBuilderWrapper.getFilter().getClass();
      List<?> filterFieldMetadata = criteriaBuilderWrapper.getFilter().getFilterFieldMetadata();
      getFilterableField(criteriaBuilderWrapper, dtoClass, filterFieldMetadata);
    }
  }

  private static void getFilterableField(
      CriteriaBuilderWrapper criteriaBuilderWrapper,
      Class<?> dtoClass,
      List<?> filterFieldMetadataList) {

    if (filterFieldMetadataList == null || filterFieldMetadataList.isEmpty()) {
      return;
    }

    List<FilterFieldMetadata> filterFieldMetadata = new ArrayList<>();
    for (Object metadataObj : filterFieldMetadataList) {
      FilterFieldMetadata metadata = (FilterFieldMetadata) metadataObj;
      String fieldName = metadata.getFieldName();

      if (fieldName == null || fieldName.isEmpty()) {
        continue;
      }

      try {
        // Check that the field exists on the DTO class
        Field field = dtoClass.getDeclaredField(fieldName);
        field.setAccessible(true);
        metadata.setField(field);
        filterFieldMetadata.add(metadata);
      } catch (NoSuchFieldException e) {
        ApplicationLogger.warn(
            "Field '{}' not found in DTO class '{}'", fieldName, dtoClass.getName());
      }
    }
    applyFilterBuilder(criteriaBuilderWrapper, filterFieldMetadata);
  }

  private static void applyFilterBuilder(
      CriteriaBuilderWrapper criteriaBuilderWrapper,
      List<FilterFieldMetadata> filterFieldMetadata) {
    Map<ExpressionOperation, List<FilterFieldMetadata>> expressionOperationMap = new HashMap<>();
    for (FilterFieldMetadata metadata : filterFieldMetadata) {
      ExpressionOperation operation = metadata.getExpressionOperation();
      expressionOperationMap.computeIfAbsent(operation, k -> new ArrayList<>()).add(metadata);
    }

    for (Map.Entry<ExpressionOperation, List<FilterFieldMetadata>> entry :
        expressionOperationMap.entrySet()) {

      ExpressionOperation op = entry.getKey();

      // Default to AND if operation is null
      if (op == null) {
        op = ExpressionOperation.AND;
      }

      List<FilterFieldMetadata> metaList = entry.getValue();
      switch (op) {
        case AND:
          List<Predicate> and = new ArrayList<>();
          for (FilterFieldMetadata meta : metaList) {
            Predicate andPredicate = buildPrediction(criteriaBuilderWrapper, meta);
            if (andPredicate != null) {
              and.add(andPredicate);
            }
          }
          Predicate finalAnd = ExpressionOperation.getJunction(op, criteriaBuilderWrapper, and);
          criteriaBuilderWrapper.and(finalAnd);
          break;
        case OR:
          List<Predicate> or = new ArrayList<>();
          for (FilterFieldMetadata meta : metaList) {
            Predicate orPredicate = buildPrediction(criteriaBuilderWrapper, meta);
            if (orPredicate != null) {
              or.add(orPredicate);
            }
          }
          Predicate finalOr = ExpressionOperation.getJunction(op, criteriaBuilderWrapper, or);
          criteriaBuilderWrapper.or(finalOr);
          break;
      }
    }
  }

  private static Predicate buildPrediction(
      CriteriaBuilderWrapper criteriaBuilderWrapper, FilterFieldMetadata metadata) {
    FilterOperator operator =
        metadata.getOperator() != null ? metadata.getOperator() : FilterOperator.EQUALS;

    Field field = metadata.getField();
    Path<?> fieldPathNode = FieldFilterUtils.getPathNode(criteriaBuilderWrapper, field);
    if (fieldPathNode == null) {
      ApplicationLogger.error("Failed to resolve path for field: " + field.getName());
      return null;
    }
    switch (operator) {
      case EQUALS, DATE_EQUALS -> {
        Object fieldValue = metadata.getFieldValue();
        if (fieldValue == null) {
          return null;
        }
        return criteriaBuilderWrapper.getEqualPredicate(fieldPathNode, fieldValue);
      }
      case NOT_EQUALS -> {
        Object fieldValue = metadata.getFieldValue();
        if (fieldValue == null) {
          return null;
        }
        return criteriaBuilderWrapper.getNotEqualPredicate(fieldPathNode, fieldValue);
      }
      case CONTAINS -> {
        Object fieldValue = metadata.getFieldValue();
        if (fieldValue == null) {
          return null;
        }
        Object value =
            FilterOperator.MatchMode.resolveString(FilterOperator.MatchMode.ANYWHERE, fieldValue);
        return criteriaBuilderWrapper.getILikePredicate(fieldPathNode, value);
      }
      case NOT_CONTAINS -> {
        Object fieldValue = metadata.getFieldValue();
        if (fieldValue == null) {
          return null;
        }
        Object value =
            FilterOperator.MatchMode.resolveString(FilterOperator.MatchMode.ANYWHERE, fieldValue);
        return criteriaBuilderWrapper.getNotILikePredicate(fieldPathNode, value);
      }
      case STARTS_WITH -> {
        Object fieldValue = metadata.getFieldValue();
        if (fieldValue == null) {
          return null;
        }
        Object value =
            FilterOperator.MatchMode.resolveString(FilterOperator.MatchMode.START, fieldValue);
        return criteriaBuilderWrapper.getILikePredicate(fieldPathNode, value);
      }
      case ENDS_WITH -> {
        Object fieldValue = metadata.getFieldValue();
        if (fieldValue == null) {
          return null;
        }
        Object value =
            FilterOperator.MatchMode.resolveString(FilterOperator.MatchMode.END, fieldValue);
        return criteriaBuilderWrapper.getILikePredicate(fieldPathNode, value);
      }
      case GREATER_THAN, DATE_AFTER -> {
        Object fieldValue = metadata.getFieldValue();
        if (fieldValue == null) {
          return null;
        }
        return criteriaBuilderWrapper.getGePredicate(fieldPathNode, fieldValue);
      }
      case GREATER_THAN_OR_EQUAL -> {
        Object fieldValue = metadata.getFieldValue();
        if (fieldValue == null) {
          return null;
        }
        return criteriaBuilderWrapper.greaterThanOrEqual(fieldPathNode, fieldValue);
      }
      case LESS_THAN, DATE_BEFORE -> {
        Object fieldValue = metadata.getFieldValue();
        if (fieldValue == null) {
          return null;
        }
        return criteriaBuilderWrapper.getLePredicate(fieldPathNode, fieldValue);
      }
      case LESS_THAN_OR_EQUAL -> {
        Object fieldValue = metadata.getFieldValue();
        if (fieldValue == null) {
          return null;
        }
        return criteriaBuilderWrapper.lessThanOrEqual(fieldPathNode, fieldValue);
      }
      case BETWEEN, DATE_BETWEEN -> {
        FilterFieldMetadata.FilterConstraints constraints = metadata.getFilterConstraints();
        if (constraints == null) {
          return null;
        }
        Object start = constraints.getMinValue();
        Object end = constraints.getMaxValue();
        if (start == null || end == null) {
          return null;
        }
        return criteriaBuilderWrapper.getBetweenPredicate(fieldPathNode, start, end);
      }
      case IN -> {
        FilterFieldMetadata.FilterConstraints constraints = metadata.getFilterConstraints();
        if (constraints == null) {
          return null;
        }
        List<Object> values = constraints.getAllowedValues();
        return criteriaBuilderWrapper.getInPredicate(fieldPathNode, values);
      }
      case NOT_IN -> {
        FilterFieldMetadata.FilterConstraints constraints = metadata.getFilterConstraints();
        if (constraints == null) {
          return null;
        }
        List<Object> values = constraints.getAllowedValues();
        return criteriaBuilderWrapper.getNotInPredicate(fieldPathNode, values);
      }
      case IS_NULL -> {
        return criteriaBuilderWrapper.getIsNullPredicate(fieldPathNode);
      }
      case IS_NOT_NULL -> {
        return criteriaBuilderWrapper.getIsNotNullPredicate(fieldPathNode);
      }
      default -> {
        return criteriaBuilderWrapper.getEqualPredicate(fieldPathNode, metadata.getFieldValue());
      }
    }
  }
}

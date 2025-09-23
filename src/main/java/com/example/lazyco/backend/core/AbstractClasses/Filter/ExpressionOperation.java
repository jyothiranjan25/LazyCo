package com.example.lazyco.backend.core.AbstractClasses.Filter;

import com.example.lazyco.backend.core.AbstractClasses.CriteriaBuilder.CriteriaBuilderWrapper;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import java.util.List;

public enum ExpressionOperation {
  AND,
  OR;

  public Predicate getJunction(
      CriteriaBuilderWrapper criteriaBuilderWrapper, List<Predicate> expressionPredicates) {
    CriteriaBuilder cb = criteriaBuilderWrapper.getCriteriaBuilder();
    if (expressionPredicates == null || expressionPredicates.isEmpty()) {
      // Decide how to handle empty list. Often returning `cb.conjunction()` is safe.
      return cb.conjunction();
    }
    return switch (this) {
      case OR -> cb.or(expressionPredicates.toArray(new Predicate[expressionPredicates.size()]));
      case AND -> cb.and(expressionPredicates.toArray(new Predicate[expressionPredicates.size()]));
    };
  }

  public static List<ExpressionOperation> getAllExpressionOperations() {
    return List.of(ExpressionOperation.values());
  }
}

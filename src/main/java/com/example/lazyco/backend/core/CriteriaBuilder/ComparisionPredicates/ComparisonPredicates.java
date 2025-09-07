package com.example.lazyco.backend.core.CriteriaBuilder.ComparisionPredicates;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;

public interface ComparisonPredicates {
  Predicate getGePredicate(
      HibernateCriteriaBuilder criteriaBuilder, Expression<?> expression, Object value);

  Predicate getLePredicate(
      HibernateCriteriaBuilder criteriaBuilder, Expression<?> expression, Object value);

  Predicate getGtPredicate(
      HibernateCriteriaBuilder criteriaBuilder, Expression<?> expression, Object value);

  Predicate getLtPredicate(
      HibernateCriteriaBuilder criteriaBuilder, Expression<?> expression, Object value);

  Predicate getBetweenPredicate(
      HibernateCriteriaBuilder criteriaBuilder, Expression<?> expression, Object from, Object to);

  Predicate getGePredicate(
      HibernateCriteriaBuilder criteriaBuilder,
      Expression<?> expression1,
      Expression<?> expression2);

  Predicate getLePredicate(
      HibernateCriteriaBuilder criteriaBuilder,
      Expression<?> expression1,
      Expression<?> expression2);

  Predicate getGtPredicate(
      HibernateCriteriaBuilder criteriaBuilder,
      Expression<?> expression1,
      Expression<?> expression2);

  Predicate getLtPredicate(
      HibernateCriteriaBuilder criteriaBuilder,
      Expression<?> expression1,
      Expression<?> expression2);

  static ComparisonPredicates factory(Object o) {
    if (o instanceof Number) {
      return new NumberComparisonPredicates();
    } else if (o instanceof Comparable) {
      return new ComparableComparisonPredicates();
    }
    throw new IllegalArgumentException("Value type not supported for comparison predicates");
  }

  static ComparisonPredicates factory(Expression<?> expression) {
    if (Number.class.isAssignableFrom(expression.getJavaType())) {
      return new NumberComparisonPredicates();
    } else if (Comparable.class.isAssignableFrom(expression.getJavaType())) {
      return new ComparableComparisonPredicates();
    }
    throw new IllegalArgumentException("Expression type not supported for comparison predicates");
  }
}

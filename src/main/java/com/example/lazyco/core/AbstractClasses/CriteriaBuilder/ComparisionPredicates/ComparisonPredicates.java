package com.example.lazyco.core.AbstractClasses.CriteriaBuilder.ComparisionPredicates;

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

  Predicate getBetweenPredicate(
      HibernateCriteriaBuilder criteriaBuilder,
      Object value,
      Expression<?> expression1,
      Expression<?> expression2);

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

  static ComparisonPredicates factory(Expression<?> expression, Object value) {
    if (Number.class.isAssignableFrom(expression.getJavaType()) && value instanceof Number) {
      return new NumberComparisonPredicates();
    } else if (Comparable.class.isAssignableFrom(expression.getJavaType())
        && value instanceof Comparable) {
      return new ComparableComparisonPredicates();
    }
    throw new IllegalArgumentException("Expression type not supported for comparison predicates");
  }

  static ComparisonPredicates factory(Expression<?> expression1, Expression<?> expression2) {
    if (Number.class.isAssignableFrom(expression1.getJavaType())
        && Number.class.isAssignableFrom(expression2.getJavaType())) {
      return new NumberComparisonPredicates();
    } else if (Comparable.class.isAssignableFrom(expression1.getJavaType())
        && Comparable.class.isAssignableFrom(expression2.getJavaType())) {
      return new ComparableComparisonPredicates();
    }
    throw new IllegalArgumentException("Expression type not supported for comparison predicates");
  }

  static ComparisonPredicates factory(Expression<?> expression, Object x, Object y) {

    if (Number.class.isAssignableFrom(expression.getJavaType())
        && x instanceof Number
        && y instanceof Number) {
      return new NumberComparisonPredicates();
    } else if (Comparable.class.isAssignableFrom(expression.getJavaType())
        && y instanceof Comparable
        && x instanceof Comparable) {
      return new ComparableComparisonPredicates();
    }
    throw new IllegalArgumentException("Value types not supported for comparison predicates");
  }

  static ComparisonPredicates factory(
      Object value, Expression<?> expression1, Expression<?> expression2) {
    if (Number.class.isAssignableFrom(expression1.getJavaType())
        && Number.class.isAssignableFrom(expression2.getJavaType())
        && value instanceof Number) {
      return new NumberComparisonPredicates();
    } else if (Comparable.class.isAssignableFrom(expression1.getJavaType())
        && Comparable.class.isAssignableFrom(expression2.getJavaType())
        && value instanceof Comparable) {
      return new ComparableComparisonPredicates();
    }
    throw new IllegalArgumentException("Value types not supported for comparison predicates");
  }
}

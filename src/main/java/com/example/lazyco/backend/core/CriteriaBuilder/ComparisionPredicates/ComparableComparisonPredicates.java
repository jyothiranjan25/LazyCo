package com.example.lazyco.backend.core.CriteriaBuilder.ComparisionPredicates;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;

public class ComparableComparisonPredicates implements ComparisonPredicates {

  public Predicate getGePredicate(
      HibernateCriteriaBuilder criteriaBuilder, Expression expression, Object value) {
    return criteriaBuilder.greaterThanOrEqualTo(expression, (Comparable) value);
  }

  public Predicate getLePredicate(
      HibernateCriteriaBuilder criteriaBuilder, Expression expression, Object value) {
    return criteriaBuilder.lessThanOrEqualTo(expression, (Comparable) value);
  }

  public Predicate getGtPredicate(
      HibernateCriteriaBuilder criteriaBuilder, Expression expression, Object value) {
    return criteriaBuilder.greaterThan(expression, (Comparable) value);
  }

  public Predicate getLtPredicate(
      HibernateCriteriaBuilder criteriaBuilder, Expression expression, Object value) {
    return criteriaBuilder.lessThan(expression, (Comparable) value);
  }

  public Predicate getBetweenPredicate(
      HibernateCriteriaBuilder criteriaBuilder, Expression expression, Object from, Object to) {
    return criteriaBuilder.between(expression, (Comparable) from, (Comparable) to);
  }

  @Override
  public Predicate getGePredicate(
      HibernateCriteriaBuilder criteriaBuilder, Expression expression1, Expression expression2) {
    return criteriaBuilder.greaterThanOrEqualTo(
        (Expression<? extends Comparable>) expression1,
        (Expression<? extends Comparable>) expression2);
  }

  @Override
  public Predicate getLePredicate(
      HibernateCriteriaBuilder criteriaBuilder, Expression expression1, Expression expression2) {
    return criteriaBuilder.lessThanOrEqualTo(
        (Expression<? extends Comparable>) expression1,
        (Expression<? extends Comparable>) expression2);
  }

  @Override
  public Predicate getGtPredicate(
      HibernateCriteriaBuilder criteriaBuilder, Expression expression1, Expression expression2) {
    return criteriaBuilder.greaterThan(
        (Expression<? extends Comparable>) expression1,
        (Expression<? extends Comparable>) expression2);
  }

  @Override
  public Predicate getLtPredicate(
      HibernateCriteriaBuilder criteriaBuilder, Expression expression1, Expression expression2) {
    return criteriaBuilder.lessThan(
        (Expression<? extends Comparable>) expression1,
        (Expression<? extends Comparable>) expression2);
  }
}

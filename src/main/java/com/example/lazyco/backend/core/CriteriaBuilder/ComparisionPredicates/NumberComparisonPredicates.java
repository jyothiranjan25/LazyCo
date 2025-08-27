package com.example.lazyco.backend.core.CriteriaBuilder.ComparisionPredicates;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;

public class NumberComparisonPredicates implements ComparisonPredicates {
  @Override
  public Predicate getGePredicate(
      HibernateCriteriaBuilder criteriaBuilder, Expression expression, Object value) {
    return criteriaBuilder.ge(expression, (Number) value);
  }

  @Override
  public Predicate getLePredicate(
      HibernateCriteriaBuilder criteriaBuilder, Expression expression, Object value) {
    return criteriaBuilder.le(expression, (Number) value);
  }

  @Override
  public Predicate getGtPredicate(
      HibernateCriteriaBuilder criteriaBuilder, Expression expression, Object value) {
    return criteriaBuilder.gt(expression, (Number) value);
  }

  @Override
  public Predicate getLtPredicate(
      HibernateCriteriaBuilder criteriaBuilder, Expression expression, Object value) {
    return criteriaBuilder.lt(expression, (Number) value);
  }

  @Override
  public Predicate getBetweenPredicate(
      HibernateCriteriaBuilder criteriaBuilder, Expression expression, Object from, Object to) {
    return criteriaBuilder.and(
        getGePredicate(criteriaBuilder, expression, from),
        getLePredicate(criteriaBuilder, expression, to));
  }

  /** below are for comparing the column with other column of the table. */
  @Override
  public Predicate getGePredicate(
      HibernateCriteriaBuilder criteriaBuilder, Expression expression1, Expression expression2) {
    return criteriaBuilder.ge(expression1, expression2);
  }

  @Override
  public Predicate getLePredicate(
      HibernateCriteriaBuilder criteriaBuilder, Expression expression1, Expression expression2) {
    return criteriaBuilder.le(expression1, expression2);
  }

  @Override
  public Predicate getGtPredicate(
      HibernateCriteriaBuilder criteriaBuilder, Expression expression1, Expression expression2) {
    return criteriaBuilder.gt(expression1, expression2);
  }

  @Override
  public Predicate getLtPredicate(
      HibernateCriteriaBuilder criteriaBuilder, Expression expression1, Expression expression2) {
    return criteriaBuilder.lt(expression1, expression2);
  }
}

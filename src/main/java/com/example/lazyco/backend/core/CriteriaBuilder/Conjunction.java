package com.example.lazyco.backend.core.CriteriaBuilder;

import jakarta.persistence.criteria.Predicate;

public class Conjunction extends JunctionBuilder {

  public Conjunction(CriteriaBuilderWrapper criteriaBuilderWrapper) {
    super(criteriaBuilderWrapper);
  }

  public Predicate build() {
    return criteriaBuilderWrapper.getAndPredicate(
        predicates.toArray(new Predicate[predicates.size()]));
  }
}

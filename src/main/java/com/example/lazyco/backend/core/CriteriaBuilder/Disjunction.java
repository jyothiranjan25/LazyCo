package com.example.lazyco.backend.core.CriteriaBuilder;

import jakarta.persistence.criteria.Predicate;

public class Disjunction extends JunctionBuilder {

  public Disjunction(CriteriaBuilderWrapper criteriaBuilderWrapper) {
    super(criteriaBuilderWrapper);
  }

  public Predicate build() {
    return criteriaBuilderWrapper.getOrPredicate(
        predicates.toArray(new Predicate[predicates.size()]));
  }
}

package com.example.lazyco.backend.core.MongoCriteriaBuilder;

import org.springframework.data.mongodb.core.query.Criteria;

public class Disjunction extends JunctionBuilder {

  public Disjunction(MongoCriteriaBuilderWrapper criteriaBuilderWrapper) {
    super(criteriaBuilderWrapper);
  }

  public Criteria build() {
    if (criteriaList == null || criteriaList.isEmpty()) {
      return new Criteria(); // empty criteria if nothing added
    }
    return criteriaBuilderWrapper.getOrPredicate(
        new Criteria().orOperator(criteriaList.toArray(new Criteria[0])));
  }
}

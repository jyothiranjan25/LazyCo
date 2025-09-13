package com.example.lazyco.backend.core.MongoCriteriaBuilder;

import org.springframework.data.mongodb.core.query.Criteria;

public class Conjunction extends JunctionBuilder {

  public Conjunction(MongoCriteriaBuilderWrapper criteriaBuilderWrapper) {
    super(criteriaBuilderWrapper);
  }

  public Criteria build() {
    if (criteriaList == null || criteriaList.isEmpty()) {
      return new Criteria(); // empty criteria if nothing added
    }
    return criteriaBuilderWrapper.getAndPredicate(
        new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
  }
}

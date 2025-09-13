package com.example.lazyco.backend.core.MongoCriteriaBuilder;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.mongodb.core.query.Criteria;

public abstract class JunctionBuilder {
  protected List<Criteria> criteriaList;

  protected MongoCriteriaBuilderWrapper criteriaBuilderWrapper;

  JunctionBuilder(MongoCriteriaBuilderWrapper criteriaBuilderWrapper) {
    this.criteriaBuilderWrapper = criteriaBuilderWrapper;
  }

  public void add(Criteria criteria) {
    if (criteriaList == null) {
      criteriaList = new ArrayList<>();
    }
    criteriaList.add(criteria);
  }
}

package com.example.lazyco.backend.core.CriteriaBuilder;

import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

public abstract class JunctionBuilder {
    protected List<Predicate> predicates;

    protected CriteriaBuilderWrapper criteriaBuilderWrapper;

    JunctionBuilder(CriteriaBuilderWrapper criteriaBuilderWrapper) {
        this.criteriaBuilderWrapper = criteriaBuilderWrapper;
    }

    public void add(Predicate predicate) {
        if (predicates == null) {
            predicates = new ArrayList<>();
        }
        predicates.add(predicate);
    }
}

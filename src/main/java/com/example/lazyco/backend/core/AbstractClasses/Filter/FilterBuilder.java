package com.example.lazyco.backend.core.AbstractClasses.Filter;

import com.example.lazyco.backend.core.AbstractClasses.CriteriaBuilder.CriteriaBuilderWrapper;
import java.util.List;

public class FilterBuilder {

  public static void build(CriteriaBuilderWrapper criteriaBuilderWrapper) {
    if (criteriaBuilderWrapper != null
        && criteriaBuilderWrapper.getFilter() != null
        && criteriaBuilderWrapper.getFilter().getFilterFieldMetadata() != null) {
      Class<?> dtoClass = criteriaBuilderWrapper.getFilter().getClass();
      List<?> filterFieldMetadata = criteriaBuilderWrapper.getFilter().getFilterFieldMetadata();
      addFilterConditions(criteriaBuilderWrapper, dtoClass, filterFieldMetadata);
    }
  }

  private static void addFilterConditions(
      CriteriaBuilderWrapper criteriaBuilderWrapper,
      Class<?> dtoClass,
      List<?> filterFieldMetadataList) {}
}

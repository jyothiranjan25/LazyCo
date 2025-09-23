package com.example.lazyco.backend.core.AbstractClasses.Filter;

import com.example.lazyco.backend.core.AbstractClasses.CriteriaBuilder.OrderType;
import java.util.List;
import java.util.Map;
import lombok.*;

@Getter
@Setter
public class FilterFieldMetadata {

  private String displayName;
  private String description;
  private Integer displayOrder;
  private String type;
  private Boolean sortable;
  private List<FilterOption> supportedOperators;
  private List<ExpressionOperation> supportedExpressions;
  private Map<String, String> enumValues;
  private List<OrderType> orderTypes;

  // New fields for enhanced filtering
  private String fieldName;
  private String fieldValue;
  private FilterOperator operator;
  private ExpressionOperation expressionOperation;
  private OrderType sortDirection;
    private FilterConstraints filterConstraints;

  @Getter
  @Setter
  public static class FilterOption {
    private String value;
    private String description;
    private FilterOperator operator;
  }

  @Getter
  @Setter
  public static class FilterConstraints {
    private Object minValue;
    private Object maxValue;
    private List<Object> allowedValues;
  }
}

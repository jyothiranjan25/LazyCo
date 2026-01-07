package com.example.lazyco.core.AbstractClasses.Filter;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.OrderType;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import lombok.*;

@Getter
@Setter
public class FilterFieldMetadata {

  private String displayName;
  private String description;
  private Integer displayOrder;
  private FieldType type;
  private String collectionElementType;
  private Boolean sortable;
  private List<FilterOption> supportedOperators;
  private List<ExpressionOperation> supportedExpressions;
  private Map<String, String> enumValues;
  private List<OrderType> orderTypes;

  // New fields for enhanced filtering
  private String fieldName;
  private Object fieldValue;
  private FilterOperator operator;
  private ExpressionOperation expressionOperation;
  private OrderType sortDirection;
  private FilterConstraints filterConstraints;

  private Field field;

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

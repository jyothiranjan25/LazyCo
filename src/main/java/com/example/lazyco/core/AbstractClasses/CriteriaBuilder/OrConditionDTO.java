package com.example.lazyco.core.AbstractClasses.CriteriaBuilder;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
public class OrConditionDTO {

  private String fullyQualifiedPath;
  private Object value;
  private conditionOperator operator;

  public OrConditionDTO(String fullyQualifiedPath, Object value) {
    this(fullyQualifiedPath, value, conditionOperator.EQUALS);
  }

  public OrConditionDTO(String fullyQualifiedPath, Object value, conditionOperator operator) {
    if (StringUtils.isBlank(fullyQualifiedPath)) {
      throw new IllegalArgumentException("Fully qualified path cannot be null or empty");
    }
    if (operator == null) {
      throw new IllegalArgumentException("Operator cannot be null");
    }
    this.fullyQualifiedPath = fullyQualifiedPath;
    this.value = value;
    this.operator = operator;
  }

  public enum conditionOperator {
    EQUALS,
    NOT_EQUALS,
    IS_NULL,
    IS_NOT_NULL,
    GREATER_THAN,
    LESS_THAN,
    GREATER_THAN_OR_EQUALS,
    LESS_THAN_OR_EQUALS,
    LIKE,
    IN,
    NOT_IN
  }
}

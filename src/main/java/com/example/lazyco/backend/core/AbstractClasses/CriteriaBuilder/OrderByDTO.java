package com.example.lazyco.backend.core.AbstractClasses.CriteriaBuilder;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
public class OrderByDTO {
  private String orderProperty;

  private OrderType orderType;

  public OrderByDTO(String orderProperty, OrderType orderType) {
    if (StringUtils.isEmpty(orderProperty)) {
      throw new IllegalArgumentException("Order property cannot be null or empty");
    }
    this.orderProperty = orderProperty;
    this.orderType = orderType == null ? OrderType.ASC : orderType;
  }
}

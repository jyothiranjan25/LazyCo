package com.example.lazyco.backend.core.AbstractClasses.CriteriaBuilder;

import java.util.List;

public enum OrderType {
  ASC,
  DESC,
  ;

  public static List<OrderType> getOrderTypes() {
    return List.of(OrderType.values());
  }
}

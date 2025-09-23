package com.example.lazyco.backend.core.AbstractClasses.CriteriaBuilder;

import com.example.lazyco.backend.core.AbstractClasses.Filter.ExpressionOperation;

import java.util.ArrayList;
import java.util.List;

public enum OrderType {
  ASC,
  DESC,
    ;
  public static List<OrderType> getOrderTypes() {
    return   List.of(OrderType.values());
  }
}

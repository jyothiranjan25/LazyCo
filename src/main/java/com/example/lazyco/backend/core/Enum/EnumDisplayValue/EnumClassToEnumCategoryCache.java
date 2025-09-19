package com.example.lazyco.backend.core.Enum.EnumDisplayValue;

import java.util.HashMap;
import java.util.Map;

public class EnumClassToEnumCategoryCache {
  private static Map<Class<? extends Enum<?>>, EnumCategory> enumClassToEnumCategoryMap;

  public static Map<Class<? extends Enum<?>>, EnumCategory> getEnumClassToEnumCategoryMap() {
    // iterate through EnumCategory enum and form the map
    if (enumClassToEnumCategoryMap == null) {
      enumClassToEnumCategoryMap = new HashMap<>();
      for (EnumCategory enumCategory : EnumCategory.values()) {
        enumClassToEnumCategoryMap.put(enumCategory.getEnumClass(), enumCategory);
      }
    }
    return enumClassToEnumCategoryMap;
  }
}

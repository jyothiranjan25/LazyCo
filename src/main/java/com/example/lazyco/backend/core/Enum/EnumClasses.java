package com.example.lazyco.backend.core.Enum;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public enum EnumClasses {
  STATUS_ENUMS("STATUS_ENUMS", StatusEnums.class),
  ;
  private final String key;
  private final Class<?> enumClass;

  EnumClasses(String key, Class<?> enumClass) {
    this.key = key;
    this.enumClass = enumClass;
  }

  private static final Map<String, EnumClasses> ENUM_MAP = new HashMap<>();

  static {
    for (EnumClasses enumClass : EnumClasses.values()) {
      ENUM_MAP.put(enumClass.getKey(), enumClass);
    }
  }

  public static EnumClasses getByKey(String key) {
    return ENUM_MAP.get(key);
  }
}

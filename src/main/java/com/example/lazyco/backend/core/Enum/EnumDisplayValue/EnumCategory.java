package com.example.lazyco.backend.core.Enum.EnumDisplayValue;

import com.example.lazyco.backend.core.Enum.StatusEnums;
import lombok.Getter;

@Getter
public enum EnumCategory {
  STATUS_ENUMS(StatusEnums.class),
  ;
  private final Class<? extends Enum<?>> enumClass;

  EnumCategory(Class<? extends Enum<?>> enumClass) {
    this.enumClass = enumClass;
  }
}

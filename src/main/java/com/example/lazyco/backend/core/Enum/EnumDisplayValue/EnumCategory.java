package com.example.lazyco.backend.core.Enum.EnumDisplayValue;

import lombok.Getter;

@Getter
public enum EnumCategory {
  ;
  private final Class<? extends Enum<?>> enumClass;

  EnumCategory(Class<? extends Enum<?>> enumClass) {
    this.enumClass = enumClass;
  }
}

package com.example.lazyco.core.Enum.EnumDisplayValue;

import com.example.lazyco.core.Utils.FieldTypeEnum;
import lombok.Getter;

@Getter
public enum EnumCategory {
  FIELD_INPUT_TYPE(FieldTypeEnum.class);
  private final Class<? extends Enum<?>> enumClass;

  EnumCategory(Class<? extends Enum<?>> enumClass) {
    this.enumClass = enumClass;
  }
}

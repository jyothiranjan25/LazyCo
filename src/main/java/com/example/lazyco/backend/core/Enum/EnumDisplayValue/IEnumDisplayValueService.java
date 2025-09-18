package com.example.lazyco.backend.core.Enum.EnumDisplayValue;

import com.example.lazyco.backend.core.AbstractClasses.Service.IAbstractService;
import java.util.List;
import java.util.Map;

public interface IEnumDisplayValueService
    extends IAbstractService<EnumDisplayValueDTO, EnumDisplayValue> {

  List<EnumDisplayValueDTO> getEnumDisplayValues(EnumCategory category);

  Map<?, ?> getEnumCodeToDisplayValueMap(Class<? extends Enum<?>> enumClass);

  Enum<?> getEnumObject(EnumCategory category, String displayValue);
}

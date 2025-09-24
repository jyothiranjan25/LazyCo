package com.example.lazyco.backend.core.Enum;

import com.example.lazyco.backend.core.Enum.EnumDisplayValue.IEnumDisplayValueService;
import com.example.lazyco.backend.core.Exceptions.ExceptionWrapper;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class EnumService {

  private final IEnumDisplayValueService enumDisplayValueService;

  public EnumService(IEnumDisplayValueService enumDisplayValueService) {
    this.enumDisplayValueService = enumDisplayValueService;
  }

  public List<EnumDTO> get(EnumDTO enumDTOs) {
    List<EnumDTO> enumDTOS = new ArrayList<>();
    enumDTOS.add(getSingle(enumDTOs));
    return enumDTOS;
  }

  @SuppressWarnings("unchecked")
  private EnumDTO getSingle(EnumDTO enumDTO) {
    if (enumDTO.getEnumType() == null) {
      throw new ExceptionWrapper("Enum Type is required.");
    }

    Class<? extends Enum<?>> enumClass;
    try {
      EnumClasses enumClasses = EnumClasses.getByKey(enumDTO.getEnumType());
      enumClass = (Class<? extends Enum<?>>) Class.forName(enumClasses.getEnumClass().getName());
    } catch (Exception e) {
      throw new ExceptionWrapper("Enum class " + enumDTO.getEnumType() + " is not found");
    }
    Map<String, String> enumMap = (Map<String, String>) getEnumMapByType(enumClass);
    enumDTO.setEnumMap(enumMap);
    return enumDTO;
  }

  @SuppressWarnings("unchecked")
  public Map<?, String> getEnumMapByType(Class<? extends Enum<?>> enumClass) {
    if (!enumClass.isEnum()) {
      throw new ExceptionWrapper("Provided class is not an enum.");
    }
    Map<?, String> originalMap =
        (Map<?, String>) enumDisplayValueService.getEnumCodeToDisplayValueMap(enumClass);
    Map<String, String> stringKeyMap = new LinkedHashMap<>();
    for (Map.Entry<?, String> entry : originalMap.entrySet()) {
      Object key = entry.getKey();
      if (key instanceof Enum) {
        stringKeyMap.put(((Enum<?>) key).name(), entry.getValue());
      } else {
        stringKeyMap.put(key.toString(), entry.getValue());
      }
    }
    return stringKeyMap;
  }
}

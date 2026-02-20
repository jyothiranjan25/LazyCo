package com.example.lazyco.entities.CustomField.CustomFieldMap;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CustomFieldContainer {

  private final Map<String, CustomFieldValueDTO> byId;
  private final Map<String, CustomFieldValueDTO> byKey;

  public CustomFieldContainer(Map<String, CustomFieldValueDTO> customFields) {
    this.byId = customFields;
    this.byKey =
        customFields.values().stream()
            .collect(Collectors.toMap(CustomFieldValueDTO::getKey, Function.identity()));
  }

  public CustomFieldValueDTO getById(String id) {
    return byId.get(id);
  }

  public CustomFieldValueDTO getByKey(String key) {
    return byKey.get(key);
  }
}

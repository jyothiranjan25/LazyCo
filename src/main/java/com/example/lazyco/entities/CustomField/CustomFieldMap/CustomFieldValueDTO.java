package com.example.lazyco.entities.CustomField.CustomFieldMap;

import com.example.lazyco.core.Utils.FieldTypeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomFieldValueDTO {
  private String name;
  private String key;
  private FieldTypeEnum fieldType;
  private Object value;
}

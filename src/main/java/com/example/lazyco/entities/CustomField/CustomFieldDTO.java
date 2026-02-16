package com.example.lazyco.entities.CustomField;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.AbstractClasses.DTO.HasName;
import com.example.lazyco.core.Utils.FieldTypeEnum;
import com.example.lazyco.entities.CustomField.CustomFieldOption.CustomFieldOptionDTO;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = CustomField.class)
public class CustomFieldDTO extends AbstractDTO<CustomFieldDTO> implements HasName {

  @InternalFilterableField private String name;
  @InternalFilterableField private String key;
  @InternalFilterableField private FieldTypeEnum fieldType;
  private List<CustomFieldOptionDTO> options;

  private String[] addOptions;
  private List<CustomFieldOptionDTO> removeOptions;
}

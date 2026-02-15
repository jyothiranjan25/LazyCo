package com.example.lazyco.entities.CustomField.CustomFieldOption;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.FieldPath;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = CustomFieldOption.class)
public class CustomFieldOptionDTO extends AbstractDTO<CustomFieldOptionDTO> {
  private String optionValue;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "customField.id")
  private Long CustomFieldId;
}

package com.example.lazyco.core.Enum.EnumDisplayValue;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = EnumDisplayValue.class)
public class EnumDisplayValueDTO extends AbstractDTO<EnumDisplayValueDTO> {

  @InternalFilterableField private String enumCode;

  @InternalFilterableField private String displayValue;

  @InternalFilterableField private EnumCategory category;
}

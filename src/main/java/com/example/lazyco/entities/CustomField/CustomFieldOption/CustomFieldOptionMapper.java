package com.example.lazyco.entities.CustomField.CustomFieldOption;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomFieldOptionMapper
    extends AbstractMapper<CustomFieldOptionDTO, CustomFieldOption> {

  @Mapping(target = "customFieldId", source = "customField.id")
  CustomFieldOptionDTO map(CustomFieldOption entity);
}

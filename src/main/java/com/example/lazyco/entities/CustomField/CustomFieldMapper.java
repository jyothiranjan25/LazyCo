package com.example.lazyco.entities.CustomField;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import com.example.lazyco.entities.CustomField.CustomFieldOption.CustomFieldOptionMapper;
import org.mapstruct.*;

@Mapper(
    componentModel = "spring",
    uses = {CustomFieldOptionMapper.class})
public interface CustomFieldMapper extends AbstractMapper<CustomFieldDTO, CustomField> {

  @Mapping(target = "options", source = "customFieldOptions")
  CustomFieldDTO map(CustomField entity);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(target = "customFieldOptions", ignore = true)
  CustomField mapDTOToEntity(CustomFieldDTO source, @MappingTarget CustomField target);
}

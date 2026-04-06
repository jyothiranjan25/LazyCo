package com.example.lazyco.entities.ApplicationForm.ApplicationFormCustomField;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ApplicationFormCustomFieldMapper
    extends AbstractMapper<ApplicationFormCustomFieldDTO, ApplicationFormCustomField> {

  @Mapping(target = "applicationFormId", source = "applicationForm.id")
  @Mapping(target = "customFieldId", source = "customField.id")
  ApplicationFormCustomFieldDTO map(ApplicationFormCustomField entity);

  default String stringify(Object value) {
    return value != null ? value.toString() : null;
  }
}

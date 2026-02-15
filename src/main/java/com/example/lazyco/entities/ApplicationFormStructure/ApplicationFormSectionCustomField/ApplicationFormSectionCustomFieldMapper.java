package com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormSectionCustomField;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ApplicationFormSectionCustomFieldMapper
    extends AbstractMapper<
        ApplicationFormSectionCustomFieldDTO, ApplicationFormSectionCustomField> {

  @Mapping(target = "applicationFormPageSectionId", source = "applicationFormPageSection.id")
  ApplicationFormSectionCustomFieldDTO map(ApplicationFormSectionCustomField entity);
}

package com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormSectionCustomField;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import com.example.lazyco.entities.CustomField.CustomFieldOption.CustomFieldOptionMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = {CustomFieldOptionMapper.class})
public interface ApplicationFormSectionCustomFieldMapper
    extends AbstractMapper<
        ApplicationFormSectionCustomFieldDTO, ApplicationFormSectionCustomField> {

  @Mapping(target = "applicationFormPageSectionId", source = "applicationFormPageSection.id")
  @Mapping(target = "customFieldId", source = "customField.id")
  @Mapping(target = "customFieldName", source = "customField.name")
  @Mapping(target = "customFieldKey", source = "customField.key")
  @Mapping(target = "customFieldFieldType", source = "customField.fieldType")
  @Mapping(target = "customFieldOptions", source = "customField.customFieldOptions")
  ApplicationFormSectionCustomFieldDTO map(ApplicationFormSectionCustomField entity);
}

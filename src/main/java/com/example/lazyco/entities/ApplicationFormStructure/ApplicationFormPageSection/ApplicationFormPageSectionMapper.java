package com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormPageSection;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormSectionCustomField.ApplicationFormSectionCustomFieldMapper;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(
    componentModel = "spring",
    uses = {ApplicationFormSectionCustomFieldMapper.class})
public interface ApplicationFormPageSectionMapper
    extends AbstractMapper<ApplicationFormPageSectionDTO, ApplicationFormPageSection> {

  @Mapping(target = "applicationFormPageId", source = "applicationFormPage.id")
  ApplicationFormPageSectionDTO map(ApplicationFormPageSection entity);

  @Named("ignoreCustomFields")
  @Mapping(target = "applicationFormPageId", source = "applicationFormPage.id")
  @Mapping(target = "applicationFormSectionCustomFields", ignore = true)
  ApplicationFormPageSectionDTO ignoreCustomFields(ApplicationFormPageSection entity);

  @Override
  default List<ApplicationFormPageSectionDTO> map(
      List<ApplicationFormPageSection> entities, ApplicationFormPageSectionDTO filter) {
    if (Boolean.FALSE.equals(filter.getFetchCustomFields())) {
      return entities.stream().map(this::ignoreCustomFields).toList();
    }
    return AbstractMapper.super.map(entities, filter);
  }
}

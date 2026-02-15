package com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormPage;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormPageSection.ApplicationFormPageSectionMapper;
import java.awt.*;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(
    componentModel = "spring",
    uses = {ApplicationFormPageSectionMapper.class})
public interface ApplicationFormPageMapper
    extends AbstractMapper<ApplicationFormPageDTO, ApplicationFormPage> {

  @Mapping(target = "applicationFormTemplateId", source = "applicationFormTemplate.id")
  ApplicationFormPageDTO map(ApplicationFormPage entity);

  @Named("ignoreSections")
  @Mapping(target = "applicationFormTemplateId", source = "applicationFormTemplate.id")
  @Mapping(target = "applicationFormPageSections", ignore = true)
  ApplicationFormPageDTO ignoreSections(ApplicationFormPage entity);

  @Override
  default List<ApplicationFormPageDTO> map(
      List<ApplicationFormPage> entities, ApplicationFormPageDTO filter) {
    if (Boolean.FALSE.equals(filter.getFetchSections())) {
      return entities.stream().map(this::ignoreSections).toList();
    }
    return AbstractMapper.super.map(entities, filter);
  }
}

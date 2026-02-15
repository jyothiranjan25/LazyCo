package com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormTemplate;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormPage.ApplicationFormPageMapper;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(
    componentModel = "spring",
    uses = {ApplicationFormPageMapper.class})
public interface ApplicationFormTemplateMapper
    extends AbstractMapper<ApplicationFormTemplateDTO, ApplicationFormTemplate> {

  @Named("ignorePage")
  @Mapping(target = "applicationFormPages", ignore = true)
  ApplicationFormTemplateDTO ignorePage(ApplicationFormTemplate entity);

  @Override
  default List<ApplicationFormTemplateDTO> map(
      List<ApplicationFormTemplate> entities, ApplicationFormTemplateDTO filter) {
    if (Boolean.FALSE.equals(filter.getFetchPages())) {
      return entities.stream().map(this::ignorePage).toList();
    }
    return AbstractMapper.super.map(entities, filter);
  }
}

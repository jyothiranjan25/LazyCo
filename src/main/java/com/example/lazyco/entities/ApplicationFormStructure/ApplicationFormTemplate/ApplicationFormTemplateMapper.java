package com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormTemplate;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormPage.ApplicationFormPageMapper;
import com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormTemplateDocument.ApplicationFormTemplateDocumentMapper;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(
    componentModel = "spring",
    uses = {ApplicationFormPageMapper.class, ApplicationFormTemplateDocumentMapper.class})
public interface ApplicationFormTemplateMapper
    extends AbstractMapper<ApplicationFormTemplateDTO, ApplicationFormTemplate> {

  @Named("ignoreDocument")
  @Mapping(target = "applicationFormTemplateDocuments", ignore = true)
  ApplicationFormTemplateDTO ignoreDocument(ApplicationFormTemplate entity);

  @Named("ignorePage")
  @Mapping(target = "applicationFormPages", ignore = true)
  ApplicationFormTemplateDTO ignorePage(ApplicationFormTemplate entity);

  @Named("ignoreDocAndPage")
  @Mapping(target = "applicationFormTemplateDocuments", ignore = true)
  @Mapping(target = "applicationFormPages", ignore = true)
  ApplicationFormTemplateDTO ignoreDocAndPage(ApplicationFormTemplate entity);

  @Override
  default List<ApplicationFormTemplateDTO> map(
      List<ApplicationFormTemplate> entities, ApplicationFormTemplateDTO filter) {
    if (Boolean.FALSE.equals(filter.getFetchDocuments())
        && Boolean.FALSE.equals(filter.getFetchPages())) {
      return entities.stream().map(this::ignoreDocAndPage).toList();
    } else if (Boolean.FALSE.equals(filter.getFetchDocuments())) {
      return entities.stream().map(this::ignoreDocument).toList();
    } else if (Boolean.FALSE.equals(filter.getFetchPages())) {
      return entities.stream().map(this::ignorePage).toList();
    }
    return AbstractMapper.super.map(entities, filter);
  }
}

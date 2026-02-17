package com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormTemplateDocument;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ApplicationFormTemplateDocumentMapper
    extends AbstractMapper<ApplicationFormTemplateDocumentDTO, ApplicationFormTemplateDocument> {

  @Mapping(target = "applicationFormTemplateId", source = "applicationFormTemplate.id")
  @Mapping(target = "documentId", source = "document.id")
  ApplicationFormTemplateDocumentDTO map(ApplicationFormTemplateDocument entity);
}

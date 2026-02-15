package com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormDocument;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ApplicationFormDocumentMapper
    extends AbstractMapper<ApplicationFormDocumentDTO, ApplicationFormDocument> {

  @Mapping(target = "applicationFormTemplateId", source = "applicationFormTemplate.id")
  @Mapping(target = "documentId", source = "document.id")
  ApplicationFormDocumentDTO map(ApplicationFormDocument entity);
}

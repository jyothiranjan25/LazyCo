package com.example.lazyco.entities.StudentFormStructure.StudentFormDocument;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StudentFormDocumentMapper
    extends AbstractMapper<StudentFormDocumentDTO, StudentFormDocument> {

  @Mapping(target = "documentId", source = "document.id")
  @Mapping(target = "documentName", source = "document.name")
  @Mapping(target = "documentKey", source = "document.key")
  @Mapping(target = "documentType", source = "document.documentType")
  StudentFormDocumentDTO map(StudentFormDocument entity);
}

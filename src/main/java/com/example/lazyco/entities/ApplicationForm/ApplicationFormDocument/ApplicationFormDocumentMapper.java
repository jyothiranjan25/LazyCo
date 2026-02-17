package com.example.lazyco.entities.ApplicationForm.ApplicationFormDocument;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ApplicationFormDocumentMapper
    extends AbstractMapper<ApplicationFormDocumentDTO, ApplicationFormDocument> {}

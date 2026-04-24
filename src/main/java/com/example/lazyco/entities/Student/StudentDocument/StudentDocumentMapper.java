package com.example.lazyco.entities.Student.StudentDocument;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StudentDocumentMapper
    extends AbstractMapper<StudentDocumentDTO, StudentDocument> {}

package com.example.lazyco.entities.Student.StudentDocument;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = StudentDocument.class)
public class StudentDocumentDTO extends AbstractDTO<StudentDocumentDTO> {}

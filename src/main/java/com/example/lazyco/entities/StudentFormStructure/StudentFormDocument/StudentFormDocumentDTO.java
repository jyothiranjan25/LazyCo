package com.example.lazyco.entities.StudentFormStructure.StudentFormDocument;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.FieldPath;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.entities.Document.DocumentTypeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = StudentFormDocument.class)
public class StudentFormDocumentDTO extends AbstractDTO<StudentFormDocumentDTO> {

  @InternalFilterableField private Boolean isMandatory;

  private Integer order;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "document.id")
  private Long documentId;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "document.name")
  private String documentName;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "document.key")
  private String documentKey;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "document.documentType")
  private DocumentTypeEnum documentType;
}

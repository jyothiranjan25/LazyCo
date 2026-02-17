package com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormTemplateDocument;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.FieldPath;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.entities.Document.DocumentTypeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = ApplicationFormTemplateDocument.class)
public class ApplicationFormTemplateDocumentDTO
    extends AbstractDTO<ApplicationFormTemplateDocumentDTO> {

  @InternalFilterableField private Boolean isMandatory;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "applicationFormTemplate.id")
  private Long applicationFormTemplateId;

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

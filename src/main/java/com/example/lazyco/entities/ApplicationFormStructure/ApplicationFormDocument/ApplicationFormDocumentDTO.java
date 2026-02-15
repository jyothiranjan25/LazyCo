package com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormDocument;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.FieldPath;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = ApplicationFormDocument.class)
public class ApplicationFormDocumentDTO extends AbstractDTO<ApplicationFormDocumentDTO> {

  @InternalFilterableField private String key;

  @InternalFilterableField private Boolean isMandatory;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "applicationFormTemplate.id")
  private Long applicationFormTemplateId;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "document.id")
  private Long documentId;
}

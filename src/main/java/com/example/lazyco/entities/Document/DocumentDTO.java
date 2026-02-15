package com.example.lazyco.entities.Document;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.AbstractClasses.DTO.HasCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = Document.class)
public class DocumentDTO extends AbstractDTO<DocumentDTO> implements HasCode {

  @InternalFilterableField private String code;

  @InternalFilterableField private DocumentTypeEnum documentType;
}

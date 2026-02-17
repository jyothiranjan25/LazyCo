package com.example.lazyco.entities.Document;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.AbstractClasses.DTO.HasName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = Document.class)
public class DocumentDTO extends AbstractDTO<DocumentDTO> implements HasName {

  @InternalFilterableField private String name;

  @InternalFilterableField private String key;

  @InternalFilterableField private DocumentTypeEnum documentType;
}

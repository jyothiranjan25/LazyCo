package com.example.lazyco.entities.ApplicationForm.ApplicationFormDocument;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = ApplicationFormDocument.class)
public class ApplicationFormDocumentDTO extends AbstractDTO<ApplicationFormDocumentDTO> {}

package com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormTemplate;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.AbstractClasses.DTO.HasCodeAndName;
import com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormPage.ApplicationFormPageDTO;
import com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormTemplateDocument.ApplicationFormTemplateDocumentDTO;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = ApplicationFormTemplate.class)
public class ApplicationFormTemplateDTO extends AbstractDTO<ApplicationFormTemplateDTO>
    implements HasCodeAndName {

  @InternalFilterableField private String code;
  @InternalFilterableField private String name;
  private String description;
  @InternalFilterableField private Boolean isActive;
  private List<ApplicationFormTemplateDocumentDTO> applicationFormTemplateDocuments;
  private List<ApplicationFormPageDTO> applicationFormPages;

  private Boolean fetchDocuments;
  private Boolean fetchPages;
}

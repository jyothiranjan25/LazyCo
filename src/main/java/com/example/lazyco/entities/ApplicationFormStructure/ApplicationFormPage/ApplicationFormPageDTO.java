package com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormPage;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.FieldPath;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.AbstractClasses.DTO.HasName;
import com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormPageSection.ApplicationFormPageSectionDTO;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = ApplicationFormPage.class)
public class ApplicationFormPageDTO extends AbstractDTO<ApplicationFormPageDTO> implements HasName {

  @InternalFilterableField private String name;
  @InternalFilterableField private String key;
  private String description;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "applicationFormTemplate.id")
  private Long applicationFormTemplateId;

  private List<ApplicationFormPageSectionDTO> applicationFormPageSections;

  private Boolean fetchSections;
}

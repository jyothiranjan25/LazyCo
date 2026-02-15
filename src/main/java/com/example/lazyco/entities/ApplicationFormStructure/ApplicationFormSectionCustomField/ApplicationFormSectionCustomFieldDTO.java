package com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormSectionCustomField;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.FieldPath;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.AbstractClasses.DTO.HasName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = ApplicationFormSectionCustomField.class)
public class ApplicationFormSectionCustomFieldDTO
    extends AbstractDTO<ApplicationFormSectionCustomFieldDTO> implements HasName {

  @InternalFilterableField private String name;
  @InternalFilterableField private String key;
  @InternalFilterableField private Boolean isRequired;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "applicationFormPageSection.id")
  private Long applicationFormPageSectionId;
}

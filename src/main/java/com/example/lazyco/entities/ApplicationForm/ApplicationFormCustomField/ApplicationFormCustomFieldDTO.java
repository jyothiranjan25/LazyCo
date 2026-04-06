package com.example.lazyco.entities.ApplicationForm.ApplicationFormCustomField;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.FieldPath;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = ApplicationFormCustomField.class)
public class ApplicationFormCustomFieldDTO extends AbstractDTO<ApplicationFormCustomFieldDTO> {

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "applicationForm.id")
  private Long applicationFormId;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "customField.id")
  private Long customFieldId;

  private String value;
}

package com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormPageSection;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.FieldPath;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.AbstractClasses.DTO.HasName;
import com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormSectionCustomField.ApplicationFormSectionCustomFieldDTO;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = ApplicationFormPageSection.class)
public class ApplicationFormPageSectionDTO extends AbstractDTO<ApplicationFormPageSectionDTO>
    implements HasName {

  @InternalFilterableField private String name;
  @InternalFilterableField private String key;
  private String description;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "applicationFormPage.id")
  private Long applicationFormPageId;

  private List<ApplicationFormSectionCustomFieldDTO> applicationFormSectionCustomFields;

  private Boolean fetchCustomFields;
}

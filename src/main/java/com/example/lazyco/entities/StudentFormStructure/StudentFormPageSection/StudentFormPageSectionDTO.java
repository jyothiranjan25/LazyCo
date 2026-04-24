package com.example.lazyco.entities.StudentFormStructure.StudentFormPageSection;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.FieldPath;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.AbstractClasses.DTO.HasName;
import com.example.lazyco.entities.StudentFormStructure.StudentFormSectionCustomField.StudentFormSectionCustomFieldDTO;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = StudentFormPageSection.class)
public class StudentFormPageSectionDTO extends AbstractDTO<StudentFormPageSectionDTO>
    implements HasName {

  @InternalFilterableField private String name;
  @InternalFilterableField private String key;
  private String description;
  private Integer order;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "studentFormPage.id")
  private Long studentFormPageId;

  private List<StudentFormSectionCustomFieldDTO> studentFormSectionCustomFields;
}

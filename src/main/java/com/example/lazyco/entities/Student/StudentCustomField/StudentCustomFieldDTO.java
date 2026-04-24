package com.example.lazyco.entities.Student.StudentCustomField;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.FieldPath;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = StudentCustomField.class)
public class StudentCustomFieldDTO extends AbstractDTO<StudentCustomFieldDTO> {

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "student.id")
  private Long studentId;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "customField.id")
  private Long customFieldId;

  private String value;
}

package com.example.lazyco.entities.StudentFormStructure.StudentFormSectionCustomField;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.FieldPath;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.Utils.FieldTypeEnum;
import com.example.lazyco.entities.CustomField.CustomFieldOption.CustomFieldOptionDTO;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = StudentFormSectionCustomField.class)
public class StudentFormSectionCustomFieldDTO
    extends AbstractDTO<StudentFormSectionCustomFieldDTO> {

  private Integer order;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "studentFormPageSection.id")
  private Long studentFormPageSectionId;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "customField.id")
  private Long customFieldId;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "customField.name")
  private String customFieldName;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "customField.key")
  private String customFieldKey;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "customField.fieldType")
  private FieldTypeEnum customFieldFieldType;

  private List<CustomFieldOptionDTO> customFieldOptions;

  private String customFieldValue;
}

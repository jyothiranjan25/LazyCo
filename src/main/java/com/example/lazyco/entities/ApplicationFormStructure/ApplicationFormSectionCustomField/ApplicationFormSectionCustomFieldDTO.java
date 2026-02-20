package com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormSectionCustomField;

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
@FilteredEntity(type = ApplicationFormSectionCustomField.class)
public class ApplicationFormSectionCustomFieldDTO
    extends AbstractDTO<ApplicationFormSectionCustomFieldDTO> {

  @InternalFilterableField private Boolean isRequired;

  @InternalFilterableField
  @FieldPath(
      fullyQualifiedPath =
          "applicationFormPageSection.applicationFormPage.applicationFormTemplate.id")
  private Long applicationFormTemplateId;

  @InternalFilterableField
  @FieldPath(
      fullyQualifiedPath =
          "applicationFormPageSection.applicationFormPage.applicationFormTemplate.admissionOffers.id")
  private List<Long> admissionOfferId;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "applicationFormPageSection.id")
  private Long applicationFormPageSectionId;

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

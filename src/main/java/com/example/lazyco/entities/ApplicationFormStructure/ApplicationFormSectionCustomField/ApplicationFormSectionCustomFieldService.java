package com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormSectionCustomField;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import org.springframework.stereotype.Service;

@Service
public class ApplicationFormSectionCustomFieldService
    extends CommonAbstractService<
        ApplicationFormSectionCustomFieldDTO, ApplicationFormSectionCustomField> {
  protected ApplicationFormSectionCustomFieldService(
      ApplicationFormSectionCustomFieldMapper applicationFormSectionCustomFieldMapper) {
    super(applicationFormSectionCustomFieldMapper);
  }
}

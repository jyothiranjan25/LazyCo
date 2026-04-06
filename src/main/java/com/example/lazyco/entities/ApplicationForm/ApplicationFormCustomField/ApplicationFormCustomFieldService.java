package com.example.lazyco.entities.ApplicationForm.ApplicationFormCustomField;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import org.springframework.stereotype.Service;

@Service
public class ApplicationFormCustomFieldService
    extends CommonAbstractService<ApplicationFormCustomFieldDTO, ApplicationFormCustomField> {
  protected ApplicationFormCustomFieldService(ApplicationFormCustomFieldMapper sampleMapper) {
    super(sampleMapper);
  }
}

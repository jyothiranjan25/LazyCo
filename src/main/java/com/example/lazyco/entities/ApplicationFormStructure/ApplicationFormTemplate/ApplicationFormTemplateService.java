package com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormTemplate;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import org.springframework.stereotype.Service;

@Service
public class ApplicationFormTemplateService
    extends CommonAbstractService<ApplicationFormTemplateDTO, ApplicationFormTemplate> {
  protected ApplicationFormTemplateService(
      ApplicationFormTemplateMapper applicationFormTemplateMapper) {
    super(applicationFormTemplateMapper);
  }
}

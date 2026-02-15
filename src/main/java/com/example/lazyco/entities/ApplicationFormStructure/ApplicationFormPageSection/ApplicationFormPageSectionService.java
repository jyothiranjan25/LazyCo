package com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormPageSection;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import org.springframework.stereotype.Service;

@Service
public class ApplicationFormPageSectionService
    extends CommonAbstractService<ApplicationFormPageSectionDTO, ApplicationFormPageSection> {
  protected ApplicationFormPageSectionService(
      ApplicationFormPageSectionMapper applicationFormPageSectionMapper) {
    super(applicationFormPageSectionMapper);
  }
}

package com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormPage;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import org.springframework.stereotype.Service;

@Service
public class ApplicationFormPageService
    extends CommonAbstractService<ApplicationFormPageDTO, ApplicationFormPage> {
  protected ApplicationFormPageService(ApplicationFormPageMapper applicationFormPageMapper) {
    super(applicationFormPageMapper);
  }
}

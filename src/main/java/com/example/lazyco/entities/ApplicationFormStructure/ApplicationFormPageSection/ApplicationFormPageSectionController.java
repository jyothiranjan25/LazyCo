package com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormPageSection;

import com.example.lazyco.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.core.AbstractClasses.Service.IAbstractService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/application_form_page_section")
public class ApplicationFormPageSectionController
    extends AbstractController<ApplicationFormPageSectionDTO> {
  public ApplicationFormPageSectionController(
      IAbstractService<ApplicationFormPageSectionDTO, ?> abstractService) {
    super(abstractService);
  }
}

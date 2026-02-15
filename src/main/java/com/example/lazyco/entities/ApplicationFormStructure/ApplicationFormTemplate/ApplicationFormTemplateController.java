package com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormTemplate;

import com.example.lazyco.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.core.AbstractClasses.Service.IAbstractService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/application_form_templates")
public class ApplicationFormTemplateController
    extends AbstractController<ApplicationFormTemplateDTO> {
  public ApplicationFormTemplateController(
      IAbstractService<ApplicationFormTemplateDTO, ?> abstractService) {
    super(abstractService);
  }
}

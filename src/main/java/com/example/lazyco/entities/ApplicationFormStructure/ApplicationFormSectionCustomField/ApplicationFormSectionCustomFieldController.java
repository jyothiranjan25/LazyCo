package com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormSectionCustomField;

import com.example.lazyco.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.core.AbstractClasses.Service.IAbstractService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/application_form_section_custom_field")
public class ApplicationFormSectionCustomFieldController
    extends AbstractController<ApplicationFormSectionCustomFieldDTO> {
  public ApplicationFormSectionCustomFieldController(
      IAbstractService<ApplicationFormSectionCustomFieldDTO, ?> abstractService) {
    super(abstractService);
  }
}

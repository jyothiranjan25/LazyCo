package com.example.lazyco.entities.ApplicationForm.ApplicationFormCustomField;

import com.example.lazyco.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.core.AbstractClasses.Service.IAbstractService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/application_form_custom_field")
public class ApplicationFormCustomFieldController
    extends AbstractController<ApplicationFormCustomFieldDTO> {
  public ApplicationFormCustomFieldController(
      IAbstractService<ApplicationFormCustomFieldDTO, ?> abstractService) {
    super(abstractService);
  }
}

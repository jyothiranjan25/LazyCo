package com.example.lazyco.entities.ApplicationForm;

import com.example.lazyco.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.core.AbstractClasses.Service.IAbstractService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/application_form")
public class ApplicationFormController extends AbstractController<ApplicationFormDTO> {

  public ApplicationFormController(IAbstractService<ApplicationFormDTO, ?> abstractService) {
    super(abstractService);
  }
}

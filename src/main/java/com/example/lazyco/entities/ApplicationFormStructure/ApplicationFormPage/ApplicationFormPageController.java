package com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormPage;

import com.example.lazyco.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.core.AbstractClasses.Service.IAbstractService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/application_form_page")
public class ApplicationFormPageController extends AbstractController<ApplicationFormPageDTO> {
  public ApplicationFormPageController(
      IAbstractService<ApplicationFormPageDTO, ?> abstractService) {
    super(abstractService);
  }
}

package com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormDocument;

import com.example.lazyco.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.core.AbstractClasses.Service.IAbstractService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/application_form_document")
public class ApplicationFormDocumentController
    extends AbstractController<ApplicationFormDocumentDTO> {
  public ApplicationFormDocumentController(
      IAbstractService<ApplicationFormDocumentDTO, ?> abstractService) {
    super(abstractService);
  }
}

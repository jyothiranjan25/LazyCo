package com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormTemplateDocument;

import com.example.lazyco.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.core.AbstractClasses.Service.IAbstractService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/application_form_template_document")
public class ApplicationFormTemplateDocumentController
    extends AbstractController<ApplicationFormTemplateDocumentDTO> {
  public ApplicationFormTemplateDocumentController(
      IAbstractService<ApplicationFormTemplateDocumentDTO, ?> abstractService) {
    super(abstractService);
  }
}

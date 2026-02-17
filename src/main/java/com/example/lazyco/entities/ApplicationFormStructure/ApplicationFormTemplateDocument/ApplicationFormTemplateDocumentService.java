package com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormTemplateDocument;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import org.springframework.stereotype.Service;

@Service
public class ApplicationFormTemplateDocumentService
    extends CommonAbstractService<
        ApplicationFormTemplateDocumentDTO, ApplicationFormTemplateDocument> {
  protected ApplicationFormTemplateDocumentService(
      ApplicationFormTemplateDocumentMapper applicationFormTemplateDocumentMapper) {
    super(applicationFormTemplateDocumentMapper);
  }
}

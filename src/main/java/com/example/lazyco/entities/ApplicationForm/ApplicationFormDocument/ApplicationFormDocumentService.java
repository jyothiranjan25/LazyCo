package com.example.lazyco.entities.ApplicationForm.ApplicationFormDocument;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import org.springframework.stereotype.Service;

@Service
public class ApplicationFormDocumentService
    extends CommonAbstractService<ApplicationFormDocumentDTO, ApplicationFormDocument> {
  protected ApplicationFormDocumentService(
      ApplicationFormDocumentMapper applicationFormDocumentMapper) {
    super(applicationFormDocumentMapper);
  }
}

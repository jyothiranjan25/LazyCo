package com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormTemplateDocument;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import com.example.lazyco.core.Exceptions.ApplicationException;
import com.example.lazyco.core.Exceptions.CommonMessage;
import org.springframework.stereotype.Service;

@Service
public class ApplicationFormTemplateDocumentService
    extends CommonAbstractService<
        ApplicationFormTemplateDocumentDTO, ApplicationFormTemplateDocument> {
  protected ApplicationFormTemplateDocumentService(
      ApplicationFormTemplateDocumentMapper applicationFormTemplateDocumentMapper) {
    super(applicationFormTemplateDocumentMapper);
  }

  @Override
  protected void validateBeforeCreate(ApplicationFormTemplateDocumentDTO request) {
    ApplicationFormTemplateDocumentDTO filter = new ApplicationFormTemplateDocumentDTO();
    filter.setDocumentId(request.getDocumentId());
    filter.setApplicationFormTemplateId(request.getApplicationFormTemplateId());
    if (getCount(filter) > 0) {
      throw new ApplicationException(
          CommonMessage.CUSTOM_MESSAGE,
          new String[] {"Document already exists for this application form template"});
    }
  }
}

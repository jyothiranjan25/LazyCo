package com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormSectionCustomField;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import com.example.lazyco.core.Exceptions.ApplicationException;
import com.example.lazyco.core.Exceptions.CommonMessage;
import org.springframework.stereotype.Service;

@Service
public class ApplicationFormSectionCustomFieldService
    extends CommonAbstractService<
        ApplicationFormSectionCustomFieldDTO, ApplicationFormSectionCustomField> {
  protected ApplicationFormSectionCustomFieldService(
      ApplicationFormSectionCustomFieldMapper applicationFormSectionCustomFieldMapper) {
    super(applicationFormSectionCustomFieldMapper);
  }

  @Override
  protected void preCreate(
      ApplicationFormSectionCustomFieldDTO request,
      ApplicationFormSectionCustomField entityToCreate) {

    ApplicationFormSectionCustomFieldDTO filter = new ApplicationFormSectionCustomFieldDTO();
    filter.setApplicationFormTemplateId(request.getApplicationFormTemplateId());
    filter.setCustomFieldId(request.getCustomFieldId());
    if (getCount(filter) > 0) {
      throw new ApplicationException(
          CommonMessage.CUSTOM_MESSAGE, new Object[] {"Custom field already exists."});
    }
  }
}

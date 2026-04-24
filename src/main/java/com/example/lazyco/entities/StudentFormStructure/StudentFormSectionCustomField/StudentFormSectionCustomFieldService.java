package com.example.lazyco.entities.StudentFormStructure.StudentFormSectionCustomField;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import com.example.lazyco.core.Exceptions.ApplicationException;
import com.example.lazyco.core.Exceptions.CommonMessage;
import org.springframework.stereotype.Service;

@Service
public class StudentFormSectionCustomFieldService
    extends CommonAbstractService<StudentFormSectionCustomFieldDTO, StudentFormSectionCustomField> {
  protected StudentFormSectionCustomFieldService(
      StudentFormSectionCustomFieldMapper studentFormSectionCustomFieldMapper) {
    super(studentFormSectionCustomFieldMapper);
  }

  @Override
  protected void preCreate(
      StudentFormSectionCustomFieldDTO request, StudentFormSectionCustomField entityToCreate) {

    StudentFormSectionCustomFieldDTO filter = new StudentFormSectionCustomFieldDTO();
    filter.setApplicationFormTemplateId(request.getApplicationFormTemplateId());
    filter.setCustomFieldId(request.getCustomFieldId());
    if (getCount(filter) > 0) {
      throw new ApplicationException(
          CommonMessage.CUSTOM_MESSAGE, new Object[] {"Custom field already exists."});
    }
  }
}

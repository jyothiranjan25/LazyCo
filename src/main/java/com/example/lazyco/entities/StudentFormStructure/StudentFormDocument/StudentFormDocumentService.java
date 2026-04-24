package com.example.lazyco.entities.StudentFormStructure.StudentFormDocument;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import com.example.lazyco.core.Exceptions.ApplicationException;
import com.example.lazyco.core.Exceptions.CommonMessage;
import org.springframework.stereotype.Service;

@Service
public class StudentFormDocumentService
    extends CommonAbstractService<StudentFormDocumentDTO, StudentFormDocument> {
  protected StudentFormDocumentService(StudentFormDocumentMapper studentFormDocumentMapper) {
    super(studentFormDocumentMapper);
  }

  @Override
  protected void validateBeforeCreate(StudentFormDocumentDTO request) {
    StudentFormDocumentDTO filter = new StudentFormDocumentDTO();
    filter.setDocumentId(request.getDocumentId());
    if (getCount(filter) > 0) {
      throw new ApplicationException(
          CommonMessage.CUSTOM_MESSAGE,
          new String[] {"Document already exists for this student template"});
    }
  }
}

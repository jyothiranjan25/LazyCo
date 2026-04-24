package com.example.lazyco.entities.Student.StudentDocument;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import org.springframework.stereotype.Service;

@Service
public class StudentDocumentService
    extends CommonAbstractService<StudentDocumentDTO, StudentDocument> {
  protected StudentDocumentService(StudentDocumentMapper studentDocumentMapper) {
    super(studentDocumentMapper);
  }
}

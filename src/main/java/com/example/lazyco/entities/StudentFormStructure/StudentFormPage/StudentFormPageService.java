package com.example.lazyco.entities.StudentFormStructure.StudentFormPage;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import org.springframework.stereotype.Service;

@Service
public class StudentFormPageService
    extends CommonAbstractService<StudentFormPageDTO, StudentFormPage> {
  protected StudentFormPageService(StudentFormPageMapper studentFormPageMapper) {
    super(studentFormPageMapper);
  }
}

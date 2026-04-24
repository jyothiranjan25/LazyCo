package com.example.lazyco.entities.Student.StudentCustomField;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import org.springframework.stereotype.Service;

@Service
public class StudentCustomFieldService
    extends CommonAbstractService<StudentCustomFieldDTO, StudentCustomField> {
  protected StudentCustomFieldService(StudentCustomFieldMapper sampleMapper) {
    super(sampleMapper);
  }
}

package com.example.lazyco.entities.StudentFormStructure.StudentFormPageSection;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import org.springframework.stereotype.Service;

@Service
public class StudentFormPageSectionService
    extends CommonAbstractService<StudentFormPageSectionDTO, StudentFormPageSection> {
  protected StudentFormPageSectionService(
      StudentFormPageSectionMapper studentFormPageSectionMapper) {
    super(studentFormPageSectionMapper);
  }
}

package com.example.lazyco.entities.SyllabusMaster.CourseCategory;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import org.springframework.stereotype.Service;

@Service
public class CourseCategoryService
    extends CommonAbstractService<CourseCategoryDTO, CourseCategory> {
  protected CourseCategoryService(CourseCategoryMapper courseCategoryMapper) {
    super(courseCategoryMapper);
  }
}

package com.example.lazyco.entities.CourseMaster.CourseClassType;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import org.springframework.stereotype.Service;

@Service
public class CourseClassTypeService
    extends CommonAbstractService<CourseClassTypeDTO, CourseClassType> {
  protected CourseClassTypeService(CourseClassTypeMapper courseClassTypeMapper) {
    super(courseClassTypeMapper);
  }
}

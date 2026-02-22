package com.example.lazyco.entities.CourseMaster.CourseArea;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import com.example.lazyco.core.Exceptions.ApplicationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class CourseAreaService extends CommonAbstractService<CourseAreaDTO, CourseArea> {
  protected CourseAreaService(CourseAreaMapper courseAreaMapper) {
    super(courseAreaMapper);
  }

  @Override
  protected void validateBeforeCreate(CourseAreaDTO request) {
    if (StringUtils.isEmpty(request.getName())) {
      throw new ApplicationException(CourseAreaMessage.COURSE_AREA_NAME_REQUIRED);
    }
    validateUniqueName(request, CourseAreaMessage.DUPLICATE_COURSE_AREA);
  }

  @Override
  protected void validateBeforeUpdate(CourseAreaDTO request) {
    if (!StringUtils.isEmpty(request.getName())) {
      validateUniqueName(request, CourseAreaMessage.DUPLICATE_COURSE_AREA);
    }
  }
}

package com.example.lazyco.entities.CourseMaster;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import com.example.lazyco.core.Exceptions.ApplicationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class CourseService extends CommonAbstractService<CourseDTO, Course> {
  protected CourseService(CourseMapper courseMapper) {
    super(courseMapper);
  }

  @Override
  protected void validateBeforeCreate(CourseDTO request) {
    if (StringUtils.isEmpty(request.getCode())) {
      throw new ApplicationException(CourseMessage.CODE_IS_REQUIRED);
    }
    validateUniqueCode(request);

    if (StringUtils.isEmpty(request.getName())) {
      throw new ApplicationException(CourseMessage.NAME_IS_REQUIRED);
    }
    validateUniqueName(request);
  }

  @Override
  protected void validateBeforeUpdate(CourseDTO request) {
    if (!StringUtils.isEmpty(request.getCode())) {
      validateUniqueCode(request);
    }

    if (!StringUtils.isEmpty(request.getName())) {
      validateUniqueName(request);
    }
  }
}

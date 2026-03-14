package com.example.lazyco.entities.CourseMaster.CourseCredit;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import org.springframework.stereotype.Service;

@Service
public class CourseCreditService extends CommonAbstractService<CourseCreditDTO, CourseCredit> {
  protected CourseCreditService(CourseCreditMapper courseCreditMapper) {
    super(courseCreditMapper);
  }
}

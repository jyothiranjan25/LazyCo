package com.example.lazyco.entities.SyllabusMaster.SyllabusOfferedCourse;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import org.springframework.stereotype.Service;

@Service
public class SyllabusOfferedCourseService
    extends CommonAbstractService<SyllabusOfferedCourseDTO, SyllabusOfferedCourse> {
  protected SyllabusOfferedCourseService(SyllabusOfferedCourseMapper syllabusOfferedCourseMapper) {
    super(syllabusOfferedCourseMapper);
  }
}

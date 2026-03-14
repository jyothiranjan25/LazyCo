package com.example.lazyco.entities.SyllabusMaster.SyllabusCourse;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import org.springframework.stereotype.Service;

@Service
public class SyllabusCourseService
    extends CommonAbstractService<SyllabusCourseDTO, SyllabusCourse> {
  protected SyllabusCourseService(SyllabusCourseMapper syllabusCourseMapper) {
    super(syllabusCourseMapper);
  }
}

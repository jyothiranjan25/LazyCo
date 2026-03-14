package com.example.lazyco.entities.SyllabusMaster.SyllabusOfferedCourse;

import com.example.lazyco.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.core.AbstractClasses.Service.IAbstractService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/syllabus_offered_course")
public class SyllabusOfferedCourseController extends AbstractController<SyllabusOfferedCourseDTO> {
  public SyllabusOfferedCourseController(
      IAbstractService<SyllabusOfferedCourseDTO, ?> abstractService) {
    super(abstractService);
  }
}

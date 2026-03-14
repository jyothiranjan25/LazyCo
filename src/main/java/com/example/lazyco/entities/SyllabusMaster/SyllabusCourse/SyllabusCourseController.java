package com.example.lazyco.entities.SyllabusMaster.SyllabusCourse;

import com.example.lazyco.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.core.AbstractClasses.Service.IAbstractService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/syllabus_course")
public class SyllabusCourseController extends AbstractController<SyllabusCourseDTO> {
  public SyllabusCourseController(IAbstractService<SyllabusCourseDTO, ?> abstractService) {
    super(abstractService);
  }
}

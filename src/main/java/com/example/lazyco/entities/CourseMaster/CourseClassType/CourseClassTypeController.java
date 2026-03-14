package com.example.lazyco.entities.CourseMaster.CourseClassType;

import com.example.lazyco.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.core.AbstractClasses.Service.IAbstractService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/course_class_type")
public class CourseClassTypeController extends AbstractController<CourseClassTypeDTO> {
  public CourseClassTypeController(IAbstractService<CourseClassTypeDTO, ?> abstractService) {
    super(abstractService);
  }
}

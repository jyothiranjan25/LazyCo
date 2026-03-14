package com.example.lazyco.entities.CourseMaster.CourseRequisite;

import com.example.lazyco.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.core.AbstractClasses.Service.IAbstractService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/course_requisite")
public class CourseRequisiteController extends AbstractController<CourseRequisiteDTO> {
  public CourseRequisiteController(IAbstractService<CourseRequisiteDTO, ?> abstractService) {
    super(abstractService);
  }
}

package com.example.lazyco.entities.CourseMaster.CourseArea;

import com.example.lazyco.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.core.AbstractClasses.Service.IAbstractService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/course_area")
public class CourseAreaController extends AbstractController<CourseAreaDTO> {
  public CourseAreaController(IAbstractService<CourseAreaDTO, ?> abstractService) {
    super(abstractService);
  }
}

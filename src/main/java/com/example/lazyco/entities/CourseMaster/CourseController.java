package com.example.lazyco.entities.CourseMaster;

import com.example.lazyco.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.core.AbstractClasses.Service.IAbstractService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/course")
public class CourseController extends AbstractController<CourseDTO> {
  public CourseController(IAbstractService<CourseDTO, ?> abstractService) {
    super(abstractService);
  }
}

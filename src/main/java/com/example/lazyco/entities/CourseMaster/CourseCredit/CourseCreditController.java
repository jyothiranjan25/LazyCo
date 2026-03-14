package com.example.lazyco.entities.CourseMaster.CourseCredit;

import com.example.lazyco.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.core.AbstractClasses.Service.IAbstractService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/course_credit")
public class CourseCreditController extends AbstractController<CourseCreditDTO> {
  public CourseCreditController(IAbstractService<CourseCreditDTO, ?> abstractService) {
    super(abstractService);
  }
}

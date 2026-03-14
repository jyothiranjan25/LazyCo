package com.example.lazyco.entities.SyllabusMaster.CourseCategory;

import com.example.lazyco.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.core.AbstractClasses.Service.IAbstractService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/course_category")
public class CourseCategoryController extends AbstractController<CourseCategoryDTO> {
  public CourseCategoryController(IAbstractService<CourseCategoryDTO, ?> abstractService) {
    super(abstractService);
  }
}

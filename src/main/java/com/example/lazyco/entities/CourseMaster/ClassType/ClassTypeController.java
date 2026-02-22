package com.example.lazyco.entities.CourseMaster.ClassType;

import com.example.lazyco.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.core.AbstractClasses.Service.IAbstractService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/class_type")
public class ClassTypeController extends AbstractController<ClassTypeDTO> {
  public ClassTypeController(IAbstractService<ClassTypeDTO, ?> abstractService) {
    super(abstractService);
  }
}

package com.example.lazyco.entities.Student;

import com.example.lazyco.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.core.AbstractClasses.Service.IAbstractService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student")
public class StudentController extends AbstractController<StudentDTO> {
  public StudentController(IAbstractService<StudentDTO, ?> abstractService) {
    super(abstractService);
  }
}

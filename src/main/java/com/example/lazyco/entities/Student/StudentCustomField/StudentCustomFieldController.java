package com.example.lazyco.entities.Student.StudentCustomField;

import com.example.lazyco.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.core.AbstractClasses.Service.IAbstractService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student_custom_field")
public class StudentCustomFieldController extends AbstractController<StudentCustomFieldDTO> {
  public StudentCustomFieldController(IAbstractService<StudentCustomFieldDTO, ?> abstractService) {
    super(abstractService);
  }
}

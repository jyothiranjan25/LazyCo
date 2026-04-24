package com.example.lazyco.entities.StudentFormStructure.StudentFormPage;

import com.example.lazyco.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.core.AbstractClasses.Service.IAbstractService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student_form_page")
public class StudentFormPageController extends AbstractController<StudentFormPageDTO> {
  public StudentFormPageController(IAbstractService<StudentFormPageDTO, ?> abstractService) {
    super(abstractService);
  }
}

package com.example.lazyco.entities.StudentFormStructure.StudentFormPageSection;

import com.example.lazyco.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.core.AbstractClasses.Service.IAbstractService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student_form_page_section")
public class StudentFormPageSectionController
    extends AbstractController<StudentFormPageSectionDTO> {
  public StudentFormPageSectionController(
      IAbstractService<StudentFormPageSectionDTO, ?> abstractService) {
    super(abstractService);
  }
}

package com.example.lazyco.entities.StudentFormStructure.StudentFormSectionCustomField;

import com.example.lazyco.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.core.AbstractClasses.Service.IAbstractService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student_form_section_custom_field")
public class StudentFormSectionCustomFieldController
    extends AbstractController<StudentFormSectionCustomFieldDTO> {
  public StudentFormSectionCustomFieldController(
      IAbstractService<StudentFormSectionCustomFieldDTO, ?> abstractService) {
    super(abstractService);
  }
}

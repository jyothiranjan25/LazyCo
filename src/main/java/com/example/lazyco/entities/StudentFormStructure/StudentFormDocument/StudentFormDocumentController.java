package com.example.lazyco.entities.StudentFormStructure.StudentFormDocument;

import com.example.lazyco.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.core.AbstractClasses.Service.IAbstractService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student_form_document")
public class StudentFormDocumentController extends AbstractController<StudentFormDocumentDTO> {
  public StudentFormDocumentController(
      IAbstractService<StudentFormDocumentDTO, ?> abstractService) {
    super(abstractService);
  }
}

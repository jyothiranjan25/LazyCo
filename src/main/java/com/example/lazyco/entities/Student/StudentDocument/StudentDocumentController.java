package com.example.lazyco.entities.Student.StudentDocument;

import com.example.lazyco.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.core.AbstractClasses.Service.IAbstractService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student_document")
public class StudentDocumentController extends AbstractController<StudentDocumentDTO> {
  public StudentDocumentController(IAbstractService<StudentDocumentDTO, ?> abstractService) {
    super(abstractService);
  }
}

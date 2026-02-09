package com.example.lazyco.entities.AcademicProgram;

import com.example.lazyco.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.core.AbstractClasses.Service.IAbstractService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/academic_program")
public class AcademicProgramController extends AbstractController<AcademicProgramDTO> {
  public AcademicProgramController(IAbstractService<AcademicProgramDTO, ?> abstractService) {
    super(abstractService);
  }
}

package com.example.lazyco.entities.AcademicProgram.ProgramTermSystem;

import com.example.lazyco.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.core.AbstractClasses.Service.IAbstractService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/program_term_system")
public class ProgramTermSystemController extends AbstractController<ProgramTermSystemDTO> {
  public ProgramTermSystemController(IAbstractService<ProgramTermSystemDTO, ?> abstractService) {
    super(abstractService);
  }
}

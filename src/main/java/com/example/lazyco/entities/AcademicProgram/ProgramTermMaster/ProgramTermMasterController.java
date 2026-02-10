package com.example.lazyco.entities.AcademicProgram.ProgramTermMaster;

import com.example.lazyco.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.core.AbstractClasses.Service.IAbstractService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/program_term_master")
public class ProgramTermMasterController extends AbstractController<ProgramTermMasterDTO> {
  public ProgramTermMasterController(IAbstractService<ProgramTermMasterDTO, ?> abstractService) {
    super(abstractService);
  }
}

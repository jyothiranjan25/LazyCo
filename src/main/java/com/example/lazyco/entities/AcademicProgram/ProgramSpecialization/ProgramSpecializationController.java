package com.example.lazyco.entities.AcademicProgram.ProgramSpecialization;

import com.example.lazyco.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.core.AbstractClasses.Service.IAbstractService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/program_specialization")
public class ProgramSpecializationController extends AbstractController<ProgramSpecializationDTO> {
  public ProgramSpecializationController(
      IAbstractService<ProgramSpecializationDTO, ?> abstractService) {
    super(abstractService);
  }
}

package com.example.lazyco.entities.ProgramCurriculum;

import com.example.lazyco.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.core.AbstractClasses.Service.IAbstractService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sample")
public class ProgramCurriculumController extends AbstractController<ProgramCurriculumDTO> {
  public ProgramCurriculumController(IAbstractService<ProgramCurriculumDTO, ?> abstractService) {
    super(abstractService);
  }
}

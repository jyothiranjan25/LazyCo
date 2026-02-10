package com.example.lazyco.entities.ProgramCycle;

import com.example.lazyco.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.core.AbstractClasses.Service.IAbstractService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/program_cycle")
public class ProgramCycleController extends AbstractController<ProgramCycleDTO> {
  public ProgramCycleController(IAbstractService<ProgramCycleDTO, ?> abstractService) {
    super(abstractService);
  }
}

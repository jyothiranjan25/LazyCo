package com.example.lazyco.entities.TermCycle;

import com.example.lazyco.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.core.AbstractClasses.Service.IAbstractService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/term_cycle")
public class TermCycleController extends AbstractController<TermCycleDTO> {
  public TermCycleController(IAbstractService<TermCycleDTO, ?> abstractService) {
    super(abstractService);
  }
}

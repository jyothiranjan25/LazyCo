package com.example.lazyco.entities.TermSystem;

import com.example.lazyco.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.core.AbstractClasses.Service.IAbstractService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/term_system")
public class TermSystemController extends AbstractController<TermSystemDTO> {
  public TermSystemController(IAbstractService<TermSystemDTO, ?> abstractService) {
    super(abstractService);
  }
}

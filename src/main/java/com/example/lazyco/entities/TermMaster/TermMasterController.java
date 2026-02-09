package com.example.lazyco.entities.TermMaster;

import com.example.lazyco.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.core.AbstractClasses.Service.IAbstractService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/term_master")
public class TermMasterController extends AbstractController<TermMasterDTO> {
  public TermMasterController(IAbstractService<TermMasterDTO, ?> abstractService) {
    super(abstractService);
  }
}

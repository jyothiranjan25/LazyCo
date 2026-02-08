package com.example.lazyco.entities.Institution;

import com.example.lazyco.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.core.AbstractClasses.Service.IAbstractService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/institution")
public class InstitutionController extends AbstractController<InstitutionDTO> {
  public InstitutionController(IAbstractService<InstitutionDTO, ?> abstractService) {
    super(abstractService);
  }
}

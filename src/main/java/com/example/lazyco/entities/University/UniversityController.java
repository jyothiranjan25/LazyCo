package com.example.lazyco.entities.University;

import com.example.lazyco.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.core.AbstractClasses.Service.IAbstractService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/university")
public class UniversityController extends AbstractController<UniversityDTO> {
  public UniversityController(IAbstractService<UniversityDTO, ?> abstractService) {
    super(abstractService);
  }
}

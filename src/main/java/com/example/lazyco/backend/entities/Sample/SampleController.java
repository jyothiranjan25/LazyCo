package com.example.lazyco.backend.entities.Sample;

import com.example.lazyco.backend.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.backend.core.AbstractClasses.Service.IAbstractService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sample")
public class SampleController extends AbstractController<SampleDTO> {
  public SampleController(IAbstractService<SampleDTO, ?> abstractService) {
    super(abstractService);
  }
}

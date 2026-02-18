package com.example.lazyco.entities.Admission;

import com.example.lazyco.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.core.AbstractClasses.Service.IAbstractService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admission")
public class AdmissionController extends AbstractController<AdmissionDTO> {
  public AdmissionController(IAbstractService<AdmissionDTO, ?> abstractService) {
    super(abstractService);
  }
}

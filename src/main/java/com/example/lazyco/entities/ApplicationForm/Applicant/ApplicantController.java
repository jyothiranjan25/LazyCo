package com.example.lazyco.entities.ApplicationForm.Applicant;

import com.example.lazyco.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.core.AbstractClasses.Service.IAbstractService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/applicat")
public class ApplicantController extends AbstractController<ApplicantDTO> {
  public ApplicantController(IAbstractService<ApplicantDTO, ?> abstractService) {
    super(abstractService);
  }
}

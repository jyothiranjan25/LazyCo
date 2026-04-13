package com.example.lazyco.entities.ApplicationForm.Applicant;

import com.example.lazyco.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.core.AbstractClasses.Service.IAbstractService;
import com.example.lazyco.core.Utils.CRUDEnums;
import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/applicat")
public class ApplicantController extends AbstractController<ApplicantDTO> {
  public ApplicantController(IAbstractService<ApplicantDTO, ?> abstractService) {
    super(abstractService);
  }

  @Override
  public List<CRUDEnums> restrictCRUDAction() {
    return List.of(CRUDEnums.POST, CRUDEnums.DELETE, CRUDEnums.PATCH);
  }
}

package com.example.lazyco.entities.AdmissionOffer.AdmissionOfferProgram;

import com.example.lazyco.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.core.AbstractClasses.Service.IAbstractService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admission_offer_program")
public class AdmissionOfferProgramController extends AbstractController<AdmissionOfferProgramDTO> {
  public AdmissionOfferProgramController(
      IAbstractService<AdmissionOfferProgramDTO, ?> abstractService) {
    super(abstractService);
  }
}

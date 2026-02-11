package com.example.lazyco.entities.AdmissionOffer;

import com.example.lazyco.core.AbstractClasses.Controller.AbstractController;
import com.example.lazyco.core.AbstractClasses.Service.IAbstractService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admission_offer")
public class AdmissionOfferController extends AbstractController<AdmissionOfferDTO> {
  public AdmissionOfferController(IAbstractService<AdmissionOfferDTO, ?> abstractService) {
    super(abstractService);
  }
}

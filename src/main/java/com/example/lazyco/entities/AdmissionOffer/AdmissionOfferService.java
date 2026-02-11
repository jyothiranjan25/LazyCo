package com.example.lazyco.entities.AdmissionOffer;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import org.springframework.stereotype.Service;

@Service
public class AdmissionOfferService
    extends CommonAbstractService<AdmissionOfferDTO, AdmissionOffer> {
  protected AdmissionOfferService(AdmissionOfferMapper admissionOfferMapper) {
    super(admissionOfferMapper);
  }
}

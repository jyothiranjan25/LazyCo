package com.example.lazyco.entities.AdmissionOffer.AdmissionOfferProgram;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import org.springframework.stereotype.Service;

@Service
public class AdmissionOfferProgramService
    extends CommonAbstractService<AdmissionOfferProgramDTO, AdmissionOfferProgram> {
  protected AdmissionOfferProgramService(AdmissionOfferProgramMapper admissionOfferProgramMapper) {
    super(admissionOfferProgramMapper);
  }
}

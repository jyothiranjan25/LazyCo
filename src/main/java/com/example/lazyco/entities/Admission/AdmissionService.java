package com.example.lazyco.entities.Admission;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import org.springframework.stereotype.Service;

@Service
public class AdmissionService extends CommonAbstractService<AdmissionDTO, Admission> {
  protected AdmissionService(AdmissionMapper admissionMapper) {
    super(admissionMapper);
  }
}

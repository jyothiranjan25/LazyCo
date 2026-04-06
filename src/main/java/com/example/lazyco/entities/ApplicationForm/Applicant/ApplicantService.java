package com.example.lazyco.entities.ApplicationForm.Applicant;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import org.springframework.stereotype.Service;

@Service
public class ApplicantService extends CommonAbstractService<ApplicantDTO, Applicant> {
  protected ApplicantService(ApplicantMapper applicantMapper) {
    super(applicantMapper);
  }
}

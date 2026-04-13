package com.example.lazyco.entities.ApplicationForm.Applicant;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import com.example.lazyco.core.Exceptions.ApplicationException;
import com.example.lazyco.core.Exceptions.CommonMessage;
import org.springframework.stereotype.Service;

@Service
public class ApplicantService extends CommonAbstractService<ApplicantDTO, Applicant> {
  protected ApplicantService(ApplicantMapper applicantMapper) {
    super(applicantMapper);
  }

  public ApplicantDTO createOrGetApplicant(ApplicantDTO applicantDTO) {
    if (applicantDTO == null
        || applicantDTO.getEmail() == null
        || applicantDTO.getPhoneNumber() == null) {
      throw new ApplicationException(
          CommonMessage.CUSTOM_MESSAGE,
          new Object[] {"ApplicantDTO must have email and phone number"});
    }
    // Try to find an existing applicant by email and phone number
    ApplicantDTO applicant = new ApplicantDTO();
    applicant.setEmail(applicantDTO.getEmail());
    applicant = getSingle(applicant);
    if (applicant != null) {
      return applicant;
    }
    applicant = new ApplicantDTO();
    applicant.setPhoneNumber(applicantDTO.getPhoneNumber());
    applicant = getSingle(applicant);
    if (applicant != null) {
      return applicant;
    }
    // create new applicant
    return executeCreateTransactional(applicantDTO);
  }

  public void deleteApplicant(ApplicantDTO applicantDTO) {
    if (applicantDTO == null || applicantDTO.getId() == null) {
      throw new ApplicationException(
          CommonMessage.CUSTOM_MESSAGE, new Object[] {"ApplicantDTO must have id"});
    }
    ApplicantDTO applicant = getById(applicantDTO.getId());
    if (applicant.getApplicationIds() == null || applicant.getApplicationIds().isEmpty()) {
      executeDeleteTransactional(applicantDTO);
    }
  }
}

package com.example.lazyco.entities.ApplicationForm;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import com.example.lazyco.entities.ApplicationForm.ApplicationFormCustomField.ApplicationFormCustomFieldMapper;
import org.mapstruct.*;

@Mapper(
    componentModel = "spring",
    uses = {ApplicationFormCustomFieldMapper.class})
public interface ApplicationFormMapper extends AbstractMapper<ApplicationFormDTO, ApplicationForm> {

  @Mapping(target = "admissionOfferId", source = "admissionOffer.id")
  @Mapping(target = "admissionOfferCode", source = "admissionOffer.code")
  @Mapping(target = "programCurriculumId", source = "programCurriculum.id")
  @Mapping(target = "programCurriculumCode", source = "programCurriculum.code")
  @Mapping(target = "startingProgramCycleId", source = "startingProgramCycle.id")
  @Mapping(target = "startingProgramCycleCode", source = "startingProgramCycle.code")
  @Mapping(target = "admissionId", source = "admission.id")
  @Mapping(target = "admissionNumber", source = "admission.admissionNumber")
  @Mapping(target = "studentId", source = "admission.student.id")
  @Mapping(target = "applicantId", source = "applicant.id")
  @Mapping(target = "email", source = "applicant.email")
  @Mapping(target = "phoneNumber", source = "applicant.phoneNumber")
  ApplicationFormDTO map(ApplicationForm entity);
}

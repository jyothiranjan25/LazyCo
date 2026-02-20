package com.example.lazyco.entities.ApplicationForm;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import com.example.lazyco.entities.CustomField.CustomFieldMap.CustomFieldValueDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ApplicationFormMapper extends AbstractMapper<ApplicationFormDTO, ApplicationForm> {

  @Mapping(target = "admissionOfferId", source = "admissionOffer.id")
  @Mapping(target = "admissionOfferCode", source = "admissionOffer.code")
  @Mapping(target = "programCurriculumId", source = "programCurriculum.id")
  @Mapping(target = "programCurriculumCode", source = "programCurriculum.code")
  @Mapping(target = "startingProgramCycleId", source = "startingProgramCycle.id")
  @Mapping(target = "startingProgramCycleCode", source = "startingProgramCycle.code")
  @Mapping(target = "admissionId" , source = "admission.id")
  @Mapping(target = "studentId" , source = "admission.student.id")
  ApplicationFormDTO map(ApplicationForm entity);

  default CustomFieldValueDTO mapToCustomFieldValueDTO(Object value) {
    if (value == null) return null;
    if (value instanceof CustomFieldValueDTO) {
      return (CustomFieldValueDTO) value;
    }
    return null;
  }
}

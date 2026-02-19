package com.example.lazyco.entities.ApplicationForm;

import com.example.lazyco.entities.Admission.AdmissionDTO;
import com.example.lazyco.entities.Admission.AdmissionMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = {ApplicationFormMapper.class, AdmissionMapper.class})
public interface ApplicationToAdmissionMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "applicationFormId", source = "id")
  @Mapping(target = "joiningProgramCycleId", source = "startingProgramCycleId")
  @Mapping(target = "currentProgramCycleId", source = "startingProgramCycleId")
  AdmissionDTO map(ApplicationFormDTO applicationFormDTO);

  @Mapping(target = "id", source = "applicationFormId")
  @Mapping(target = "startingProgramCycleId", source = "joiningProgramCycleId")
  ApplicationFormDTO map(AdmissionDTO admissionDTO);
}

package com.example.lazyco.entities.ApplicationForm;

import com.example.lazyco.entities.Admission.AdmissionDTO;
import com.example.lazyco.entities.Admission.AdmissionMapper;
import org.mapstruct.*;

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
  @Mapping(target = "admissionId", source = "id")
  @Mapping(target = "startingProgramCycleId", source = "joiningProgramCycleId")
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  ApplicationFormDTO map(
      AdmissionDTO admissionDTO, @MappingTarget ApplicationFormDTO applicationFormDTO);
}

package com.example.lazyco.entities.Admission;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AdmissionMapper extends AbstractMapper<AdmissionDTO, Admission> {

  @Mapping(target = "applicationFormId", source = "applicationForm.id")
  @Mapping(target = "programCurriculumId", source = "programCurriculum.id")
  @Mapping(target = "joiningProgramCycleId", source = "joiningProgramCycle.id")
  @Mapping(target = "currentProgramCycleId", source = "currentProgramCycle.id")
  @Mapping(target = "studentId", source = "student.id")
  AdmissionDTO map(Admission entity);
}

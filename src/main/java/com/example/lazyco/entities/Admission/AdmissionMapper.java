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
  @Mapping(target = "firstName", source = "student.firstName")
  @Mapping(target = "middleName", source = "student.middleName")
  @Mapping(target = "lastName", source = "student.lastName")
  @Mapping(target = "gender", source = "student.gender")
  @Mapping(target = "dateOfBirth", source = "student.dateOfBirth")
  @Mapping(target = "email", source = "student.email")
  @Mapping(target = "phoneNumber", source = "student.phoneNumber")
  AdmissionDTO map(Admission entity);
}

package com.example.lazyco.entities.AcademicProgram.ProgramSpecialization;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProgramSpecializationMapper
    extends AbstractMapper<ProgramSpecializationDTO, ProgramSpecialization> {

  @Mapping(target = "academicProgramId", source = "academicProgram.id")
  @Mapping(target = "academicProgramCode", source = "academicProgram.code")
  @Mapping(target = "academicProgramName", source = "academicProgram.name")
  ProgramSpecializationDTO map(ProgramSpecialization entity);
}

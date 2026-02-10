package com.example.lazyco.entities.AcademicProgram.ProgramSpecialization;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProgramSpecializationMapper
    extends AbstractMapper<ProgramSpecializationDTO, ProgramSpecialization> {

  @Mapping(target = "academicProgramId", source = "academicProgram.id")
  ProgramSpecializationDTO map(ProgramSpecialization entity);
}

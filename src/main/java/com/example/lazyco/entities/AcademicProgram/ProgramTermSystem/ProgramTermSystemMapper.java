package com.example.lazyco.entities.AcademicProgram.ProgramTermSystem;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProgramTermSystemMapper
    extends AbstractMapper<ProgramTermSystemDTO, ProgramTermSystem> {

  @Mapping(target = "academicProgramId", source = "academicProgram.id")
  @Mapping(target = "academicProgramCode", source = "academicProgram.code")
  @Mapping(target = "academicProgramName", source = "academicProgram.name")
  ProgramTermSystemDTO map(ProgramTermSystem entity);
}

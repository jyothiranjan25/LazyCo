package com.example.lazyco.entities.AcademicProgram.ProgramTermSystem;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProgramTermSystemMapper
    extends AbstractMapper<ProgramTermSystemDTO, ProgramTermSystem> {

  @Mapping(target = "academicProgramId", source = "academicProgram.id")
  ProgramTermSystemDTO map(ProgramTermSystem entity);
}

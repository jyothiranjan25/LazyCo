package com.example.lazyco.entities.AcademicProgram.ProgramTermMaster;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProgramTermMasterMapper
    extends AbstractMapper<ProgramTermMasterDTO, ProgramTermMaster> {

  @Mapping(target = "programTermSystemId", source = "programTermSystem.id")
  ProgramTermMasterDTO map(ProgramTermMaster entity);
}

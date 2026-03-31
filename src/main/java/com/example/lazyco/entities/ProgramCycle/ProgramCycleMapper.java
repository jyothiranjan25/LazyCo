package com.example.lazyco.entities.ProgramCycle;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProgramCycleMapper extends AbstractMapper<ProgramCycleDTO, ProgramCycle> {

  @Mapping(target = "programCurriculumId", source = "programCurriculum.id")
  @Mapping(target = "termCycleId", source = "termCycle.id")
  @Mapping(target = "programTermMasterId", source = "programTermMaster.id")
  @Mapping(target = "syllabusMasterId", source = "syllabusMaster.id")
  ProgramCycleDTO map(ProgramCycle entity);
}

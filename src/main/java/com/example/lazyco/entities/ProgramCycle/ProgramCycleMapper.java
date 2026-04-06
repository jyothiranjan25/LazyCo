package com.example.lazyco.entities.ProgramCycle;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProgramCycleMapper extends AbstractMapper<ProgramCycleDTO, ProgramCycle> {

  @Mapping(target = "programCurriculumId", source = "programCurriculum.id")
  @Mapping(target = "programCurriculumCode", source = "programCurriculum.code")
  @Mapping(target = "programCurriculumName", source = "programCurriculum.code")
  @Mapping(target = "termCycleId", source = "termCycle.id")
  @Mapping(target = "termCycleCode", source = "termCycle.code")
  @Mapping(target = "termCycleName", source = "termCycle.code")
  @Mapping(target = "programTermMasterId", source = "programTermMaster.id")
  @Mapping(target = "programTermMasterName", source = "programTermMaster.name")
  @Mapping(target = "syllabusMasterId", source = "syllabusMaster.id")
  @Mapping(target = "syllabusMasterCode", source = "syllabusMaster.code")
  @Mapping(target = "syllabusMasterName", source = "syllabusMaster.name")
  ProgramCycleDTO map(ProgramCycle entity);
}

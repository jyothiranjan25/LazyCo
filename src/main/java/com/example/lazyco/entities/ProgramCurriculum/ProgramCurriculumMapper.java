package com.example.lazyco.entities.ProgramCurriculum;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProgramCurriculumMapper
    extends AbstractMapper<ProgramCurriculumDTO, ProgramCurriculum> {

  @Mapping(target = "academicYearId", source = "academicYear.id")
  @Mapping(target = "academicYearCode", source = "academicYear.code")
  @Mapping(target = "academicYearName", source = "academicYear.name")
  @Mapping(target = "termSystemId", source = "termSystem.id")
  @Mapping(target = "termSystemCode", source = "termSystem.code")
  @Mapping(target = "termSystemName", source = "termSystem.name")
  @Mapping(target = "academicProgramId", source = "academicProgram.id")
  @Mapping(target = "academicProgramCode", source = "academicProgram.code")
  @Mapping(target = "academicProgramName", source = "academicProgram.name")
  @Mapping(target = "programTermSystemId", source = "programTermSystem.id")
  @Mapping(target = "programTermSystemCode", source = "programTermSystem.code")
  @Mapping(target = "programTermSystemName", source = "programTermSystem.name")
  ProgramCurriculumDTO map(ProgramCurriculum entity);
}

package com.example.lazyco.entities.ProgramCurriculum;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProgramCurriculumMapper
    extends AbstractMapper<ProgramCurriculumDTO, ProgramCurriculum> {

  @Mapping(target = "academicYearId", source = "academicYear.id")
  @Mapping(target = "termSystemId", source = "termSystem.id")
  @Mapping(target = "academicProgramId", source = "academicProgram.id")
  @Mapping(target = "programTermSystemId", source = "programTermSystem.id")
  ProgramCurriculumDTO map(ProgramCurriculum entity);
}

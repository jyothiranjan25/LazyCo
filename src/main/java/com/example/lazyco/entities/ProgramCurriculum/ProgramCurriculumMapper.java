package com.example.lazyco.entities.ProgramCurriculum;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProgramCurriculumMapper
    extends AbstractMapper<ProgramCurriculumDTO, ProgramCurriculum> {}

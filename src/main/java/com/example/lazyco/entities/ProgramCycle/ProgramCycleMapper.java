package com.example.lazyco.entities.ProgramCycle;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProgramCycleMapper extends AbstractMapper<ProgramCycleDTO, ProgramCycle> {}

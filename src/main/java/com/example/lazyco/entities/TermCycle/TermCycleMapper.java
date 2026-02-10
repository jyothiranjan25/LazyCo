package com.example.lazyco.entities.TermCycle;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TermCycleMapper extends AbstractMapper<TermCycleDTO, TermCycle> {}

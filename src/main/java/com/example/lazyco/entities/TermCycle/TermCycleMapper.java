package com.example.lazyco.entities.TermCycle;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TermCycleMapper extends AbstractMapper<TermCycleDTO, TermCycle> {

  @Mapping(target = "academicYearId", source = "academicYear.id")
  @Mapping(target = "termMasterId", source = "termMaster.id")
  TermCycleDTO map(TermCycle entity);
}

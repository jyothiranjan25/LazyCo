package com.example.lazyco.entities.TermCycle;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface TermCycleMapper extends AbstractMapper<TermCycleDTO, TermCycle> {

    @Mapping(target = "academicYearId", source = "academicYear.id")
    @Mapping(target = "termMasterId", source = "termMaster.id")
    TermCycleDTO map(TermCycle entity);
}

package com.example.lazyco.entities.TermSystem.TermMaster;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TermMasterMapper extends AbstractMapper<TermMasterDTO, TermMaster> {

  @Mapping(target = "termSystemId", source = "termSystem.id")
  TermMasterDTO map(TermMaster entity);
}

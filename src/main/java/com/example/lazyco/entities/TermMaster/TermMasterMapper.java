package com.example.lazyco.entities.TermMaster;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TermMasterMapper extends AbstractMapper<TermMasterDTO, TermMaster> {

  @Mapping(target = "termSystemId", source = "termSystem.id")
  TermMasterDTO map(TermMaster entity);

  @Mapping(target = "termSystem", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  TermMaster mapDTOToEntity(TermMasterDTO source, @MappingTarget TermMaster target);
}

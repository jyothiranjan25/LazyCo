package com.example.lazyco.entities.Institution;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface InstitutionMapper extends AbstractMapper<InstitutionDTO, Institution> {

  @Mapping(target = "universityId", source = "university.id")
  @Mapping(target = "universityCode", source = "university.code")
  @Mapping(target = "universityName", source = "university.name")
  InstitutionDTO map(Institution entity);
}

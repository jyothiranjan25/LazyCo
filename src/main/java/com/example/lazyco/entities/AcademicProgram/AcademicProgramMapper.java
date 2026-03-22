package com.example.lazyco.entities.AcademicProgram;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AcademicProgramMapper extends AbstractMapper<AcademicProgramDTO, AcademicProgram> {

  @Mapping(target = "institutionId", source = "institution.id")
  @Mapping(target = "institutionCode", source = "institution.code")
  @Mapping(target = "institutionName", source = "institution.name")
  AcademicProgramDTO map(AcademicProgram entity);
}

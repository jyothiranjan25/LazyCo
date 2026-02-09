package com.example.lazyco.entities.AcademicProgram;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AcademicProgramMapper extends AbstractMapper<AcademicProgramDTO, AcademicProgram> {

  @Mapping(target = "institutionId", source = "institution.id")
  AcademicProgramDTO map(AcademicProgram entity);
}

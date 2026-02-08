package com.example.lazyco.entities.University;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UniversityMapper extends AbstractMapper<UniversityDTO, University> {}

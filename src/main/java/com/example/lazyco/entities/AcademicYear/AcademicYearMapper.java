package com.example.lazyco.entities.AcademicYear;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AcademicYearMapper extends AbstractMapper<AcademicYearDTO, AcademicYear> {}

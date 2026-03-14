package com.example.lazyco.entities.SyllabusMaster.SyllabusOfferedCourse;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SyllabusOfferedCourseMapper
    extends AbstractMapper<SyllabusOfferedCourseDTO, SyllabusOfferedCourse> {}

package com.example.lazyco.entities.SyllabusMaster.SyllabusCourse;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SyllabusCourseMapper extends AbstractMapper<SyllabusCourseDTO, SyllabusCourse> {}

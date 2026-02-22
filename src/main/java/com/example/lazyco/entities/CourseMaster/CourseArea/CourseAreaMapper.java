package com.example.lazyco.entities.CourseMaster.CourseArea;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseAreaMapper extends AbstractMapper<CourseAreaDTO, CourseArea> {}

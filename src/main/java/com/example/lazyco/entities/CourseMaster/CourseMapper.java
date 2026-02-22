package com.example.lazyco.entities.CourseMaster;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseMapper extends AbstractMapper<CourseDTO, Course> {}

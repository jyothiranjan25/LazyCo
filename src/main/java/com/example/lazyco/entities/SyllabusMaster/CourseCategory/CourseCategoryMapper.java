package com.example.lazyco.entities.SyllabusMaster.CourseCategory;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseCategoryMapper extends AbstractMapper<CourseCategoryDTO, CourseCategory> {}

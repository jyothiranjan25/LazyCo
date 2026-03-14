package com.example.lazyco.entities.CourseMaster.CourseRequisite;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseRequisiteMapper
    extends AbstractMapper<CourseRequisiteDTO, CourseRequisite> {}

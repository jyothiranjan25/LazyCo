package com.example.lazyco.entities.CourseMaster;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CourseMapper extends AbstractMapper<CourseDTO, Course> {

  @Mapping(target = "courseAreaId", source = "courseArea.id")
  @Mapping(target = "courseAreaName", source = "courseArea.name")
  @Mapping(target = "courseAreaDescription", source = "courseArea.description")
  @Mapping(target = "institutionId", source = "institution.id")
  @Mapping(target = "institutionCode", source = "institution.code")
  @Mapping(target = "institutionName", source = "institution.name")
  CourseDTO map(Course entity);
}

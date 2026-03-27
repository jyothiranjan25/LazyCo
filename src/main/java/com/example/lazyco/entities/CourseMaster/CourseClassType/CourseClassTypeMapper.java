package com.example.lazyco.entities.CourseMaster.CourseClassType;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CourseClassTypeMapper extends AbstractMapper<CourseClassTypeDTO, CourseClassType> {

  @Mapping(target = "courseId", source = "course.id")
  @Mapping(target = "courseCode", source = "course.code")
  @Mapping(target = "courseName", source = "course.name")
  @Mapping(target = "classTypeId", source = "classType.id")
  @Mapping(target = "classTypeName", source = "classType.name")
  CourseClassTypeDTO map(CourseClassType entity);
}

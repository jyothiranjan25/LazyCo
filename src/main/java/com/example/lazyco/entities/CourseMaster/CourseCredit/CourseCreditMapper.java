package com.example.lazyco.entities.CourseMaster.CourseCredit;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CourseCreditMapper extends AbstractMapper<CourseCreditDTO, CourseCredit> {

  @Mapping(target = "courseId", source = "course.id")
  @Mapping(target = "courseCode", source = "course.code")
  @Mapping(target = "courseName", source = "course.name")
  CourseCreditDTO map(CourseCredit entity);
}

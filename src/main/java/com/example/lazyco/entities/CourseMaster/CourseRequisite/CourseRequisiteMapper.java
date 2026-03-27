package com.example.lazyco.entities.CourseMaster.CourseRequisite;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CourseRequisiteMapper extends AbstractMapper<CourseRequisiteDTO, CourseRequisite> {

  @Mapping(target = "courseId", source = "course.id")
  @Mapping(target = "courseCode", source = "course.code")
  @Mapping(target = "courseName", source = "course.name")
  @Mapping(target = "requisiteCourseId", source = "requisiteCourse.id")
  @Mapping(target = "requisiteCourseCode", source = "requisiteCourse.code")
  @Mapping(target = "requisiteCourseName", source = "requisiteCourse.name")
  CourseRequisiteDTO map(CourseRequisite entity);
}

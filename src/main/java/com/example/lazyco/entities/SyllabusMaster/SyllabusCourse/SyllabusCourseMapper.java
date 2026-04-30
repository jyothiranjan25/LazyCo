package com.example.lazyco.entities.SyllabusMaster.SyllabusCourse;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SyllabusCourseMapper extends AbstractMapper<SyllabusCourseDTO, SyllabusCourse> {

  @Mapping(target = "syllabusMasterId", source = "syllabusMaster.id")
  @Mapping(target = "courseCategoryId", source = "courseCategory.id")
  @Mapping(target = "courseCategoryName", source = "courseCategory.name")
  @Mapping(target = "courseCreditId", source = "courseCredit.id")
  @Mapping(target = "courseCredit", source = "courseCredit.credit")
  @Mapping(target = "courseId", source = "courseCredit.course.id")
  @Mapping(target = "courseCode", source = "courseCredit.course.code")
  @Mapping(target = "courseName", source = "courseCredit.course.name")
  SyllabusCourseDTO map(SyllabusCourse entity);
}

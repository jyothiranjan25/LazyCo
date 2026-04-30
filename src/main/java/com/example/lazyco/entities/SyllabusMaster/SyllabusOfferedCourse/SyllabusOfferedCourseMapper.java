package com.example.lazyco.entities.SyllabusMaster.SyllabusOfferedCourse;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SyllabusOfferedCourseMapper
    extends AbstractMapper<SyllabusOfferedCourseDTO, SyllabusOfferedCourse> {

  @Mapping(target = "syllabusCourseId", source = "syllabusCourse.id")
  @Mapping(target = "courseClassTypeId", source = "courseClassType.id")
  @Mapping(target = "courseClassTypeName", source = "courseClassType.classType.name")
  SyllabusOfferedCourseDTO map(SyllabusOfferedCourse entity);
}

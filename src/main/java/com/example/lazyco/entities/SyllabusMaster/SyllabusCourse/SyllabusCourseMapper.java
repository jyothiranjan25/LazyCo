package com.example.lazyco.entities.SyllabusMaster.SyllabusCourse;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SyllabusCourseMapper extends AbstractMapper<SyllabusCourseDTO, SyllabusCourse> {

  @Mapping(target = "syllabusMasterId", source = "syllabusMaster.id")
  @Mapping(target = "courseCategoryId", source = "courseCategory.id")
  @Mapping(target = "courseCreditId", source = "courseCredit.id")
  SyllabusCourseDTO map(SyllabusCourse entity);
}

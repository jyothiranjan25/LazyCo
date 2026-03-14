package com.example.lazyco.entities.CourseMaster.CourseCredit;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseCreditMapper extends AbstractMapper<CourseCreditDTO, CourseCredit> {}

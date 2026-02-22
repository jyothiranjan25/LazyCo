package com.example.lazyco.entities.CourseMaster.ClassType;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClassTypeMapper extends AbstractMapper<ClassTypeDTO, ClassType> {}

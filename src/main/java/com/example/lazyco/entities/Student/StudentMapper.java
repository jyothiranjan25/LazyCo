package com.example.lazyco.entities.Student;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StudentMapper extends AbstractMapper<StudentDTO, Student> {}

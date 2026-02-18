package com.example.lazyco.entities.Student;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import com.example.lazyco.entities.Admission.AdmissionMapper;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = {AdmissionMapper.class})
public interface StudentMapper extends AbstractMapper<StudentDTO, Student> {}

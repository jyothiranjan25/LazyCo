package com.example.lazyco.entities.Admission;

import com.example.lazyco.entities.Student.StudentDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AdmissionToStudentMapper {

  @Mapping(target = "id", ignore = true)
  StudentDTO map(AdmissionDTO admissionDTO);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "studentId", source = "id")
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  AdmissionDTO map(StudentDTO studentDTO, @MappingTarget AdmissionDTO admissionDTO);
}

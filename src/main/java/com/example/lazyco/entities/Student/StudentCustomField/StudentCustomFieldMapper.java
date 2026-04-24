package com.example.lazyco.entities.Student.StudentCustomField;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StudentCustomFieldMapper
    extends AbstractMapper<StudentCustomFieldDTO, StudentCustomField> {

  @Mapping(target = "studentId", source = "student.id")
  @Mapping(target = "customFieldId", source = "customField.id")
  StudentCustomFieldDTO map(StudentCustomField entity);

  default String stringify(Object value) {
    return value != null ? value.toString() : null;
  }
}

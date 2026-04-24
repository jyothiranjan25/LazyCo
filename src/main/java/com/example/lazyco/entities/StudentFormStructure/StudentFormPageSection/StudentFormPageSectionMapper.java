package com.example.lazyco.entities.StudentFormStructure.StudentFormPageSection;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import com.example.lazyco.entities.StudentFormStructure.StudentFormSectionCustomField.StudentFormSectionCustomFieldMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = {StudentFormSectionCustomFieldMapper.class})
public interface StudentFormPageSectionMapper
    extends AbstractMapper<StudentFormPageSectionDTO, StudentFormPageSection> {

  @Mapping(target = "studentFormPageId", source = "studentFormPage.id")
  StudentFormPageSectionDTO map(StudentFormPageSection entity);
}

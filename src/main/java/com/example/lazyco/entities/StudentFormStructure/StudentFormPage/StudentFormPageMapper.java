package com.example.lazyco.entities.StudentFormStructure.StudentFormPage;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import com.example.lazyco.entities.StudentFormStructure.StudentFormPageSection.StudentFormPageSectionMapper;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = {StudentFormPageSectionMapper.class})
public interface StudentFormPageMapper
    extends AbstractMapper<StudentFormPageDTO, StudentFormPage> {}

package com.example.lazyco.backend.entities.UserManagement.Sample;

import com.example.lazyco.backend.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SampleMapper extends AbstractMapper<SampleDTO, Sample> {}

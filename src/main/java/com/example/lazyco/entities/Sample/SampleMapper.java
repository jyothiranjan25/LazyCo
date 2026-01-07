package com.example.lazyco.backend.entities.Sample;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SampleMapper extends AbstractMapper<SampleDTO, Sample> {}

package com.example.lazyco.Sample;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SampleMapper extends AbstractMapper<SampleDTO, Sample> {}

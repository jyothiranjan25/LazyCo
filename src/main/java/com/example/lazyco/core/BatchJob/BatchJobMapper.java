package com.example.lazyco.core.BatchJob;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BatchJobMapper extends AbstractMapper<BatchJobDTO, BatchJob> {}

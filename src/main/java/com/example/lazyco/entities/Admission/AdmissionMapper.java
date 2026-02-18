package com.example.lazyco.entities.Admission;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdmissionMapper extends AbstractMapper<AdmissionDTO, Admission> {}

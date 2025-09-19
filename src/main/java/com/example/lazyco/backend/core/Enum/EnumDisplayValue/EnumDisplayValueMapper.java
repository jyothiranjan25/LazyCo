package com.example.lazyco.backend.core.Enum.EnumDisplayValue;

import com.example.lazyco.backend.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EnumDisplayValueMapper
    extends AbstractMapper<EnumDisplayValueDTO, EnumDisplayValue> {}

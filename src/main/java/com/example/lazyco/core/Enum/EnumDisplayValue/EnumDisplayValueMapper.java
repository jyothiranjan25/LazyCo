package com.example.lazyco.core.Enum.EnumDisplayValue;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EnumDisplayValueMapper
    extends AbstractMapper<EnumDisplayValueDTO, EnumDisplayValue> {}

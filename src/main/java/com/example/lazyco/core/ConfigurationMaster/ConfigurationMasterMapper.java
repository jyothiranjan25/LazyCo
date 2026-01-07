package com.example.lazyco.core.ConfigurationMaster;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConfigurationMasterMapper
    extends AbstractMapper<ConfigurationMasterDTO, ConfigurationMaster> {}

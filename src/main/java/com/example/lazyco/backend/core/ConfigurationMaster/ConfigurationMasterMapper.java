package com.example.lazyco.backend.core.ConfigurationMaster;

import com.example.lazyco.backend.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConfigurationMasterMapper
    extends AbstractMapper<ConfigurationMasterDTO, ConfigurationMaster> {}

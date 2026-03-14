package com.example.lazyco.entities.SyllabusMaster;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SyllabusMasterMapper extends AbstractMapper<SyllabusMasterDTO, SyllabusMaster> {}

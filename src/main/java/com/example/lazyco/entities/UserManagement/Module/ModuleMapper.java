package com.example.lazyco.entities.UserManagement.Module;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ModuleMapper extends AbstractMapper<ModuleDTO, Module> {}

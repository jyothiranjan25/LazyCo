package com.example.lazyco.backend.entities.UserManagement.Resource;

import com.example.lazyco.backend.core.AbstractDocClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ResourceMapper extends AbstractMapper<ResourceDTO, Resource> {}

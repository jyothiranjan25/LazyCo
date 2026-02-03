package com.example.lazyco.entities.UserManagement.Resource;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ResourceMapper extends AbstractMapper<ResourceDTO, Resource> {

  @Mapping(source = "parentResource.id", target = "parentId")
  ResourceDTO map(Resource entity);
}

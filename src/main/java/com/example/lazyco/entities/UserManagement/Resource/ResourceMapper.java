package com.example.lazyco.entities.UserManagement.Resource;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import java.util.List;

import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ResourceMapper extends AbstractMapper<ResourceDTO, Resource> {

  @Mapping(source = "parentResource.id", target = "parentId")
  @Mapping(target = "childResources", ignore = true)
  ResourceDTO map(Resource entity);

  @Mapping(target = "parentResource", ignore = true)
  @Mapping(target = "childResources", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  Resource mapDTOToEntity(ResourceDTO source,@MappingTarget Resource target);

  @Named("mapChildForParent")
  @Mapping(source = "parentResource.id", target = "parentId")
  ResourceDTO mapChildForParent(Resource entity);

  @Override
  default List<ResourceDTO> map(List<Resource> entities, ResourceDTO filter) {
    if (Boolean.TRUE.equals(filter.getGetChildForParent())) {
      return entities.stream().map(this::mapChildForParent).toList();
    }
    return AbstractMapper.super.map(entities, filter);
  }
}

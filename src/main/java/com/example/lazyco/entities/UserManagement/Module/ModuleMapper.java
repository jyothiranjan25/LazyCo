package com.example.lazyco.entities.UserManagement.Module;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import com.example.lazyco.entities.UserManagement.Resource.ResourceMapper;
import java.util.List;
import org.mapstruct.*;

@Mapper(
    componentModel = "spring",
    uses = {ResourceMapper.class})
public interface ModuleMapper extends AbstractMapper<ModuleDTO, Module> {

  @Mapping(target = "resources", ignore = true)
  ModuleDTO map(Module entity);

  @Named("mapResources")
  ModuleDTO mapResources(Module entity);

  @Override
  default List<ModuleDTO> map(List<Module> entities, ModuleDTO filter) {
    if (filter != null && Boolean.TRUE.equals(filter.getFetchResources())) {
      return entities.stream().map(this::mapResources).toList();
    }
    return AbstractMapper.super.map(entities, filter);
  }
}

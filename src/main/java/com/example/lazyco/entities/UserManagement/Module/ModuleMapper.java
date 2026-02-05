package com.example.lazyco.entities.UserManagement.Module;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import com.example.lazyco.entities.UserManagement.Resource.ResourceMapper;
import java.util.List;
import org.mapstruct.*;

@Mapper(
    componentModel = "spring",
    uses = {ResourceMapper.class})
public interface ModuleMapper extends AbstractMapper<ModuleDTO, Module> {

  @Named("ignoreResources")
  @Mapping(target = "resources", ignore = true)
  ModuleDTO mapIgnoringResources(Module entity);

  @Override
  default List<ModuleDTO> map(List<Module> entities, ModuleDTO filter) {
    if (filter != null && Boolean.FALSE.equals(filter.getFetchResources())) {
      return entities.stream().map(this::mapIgnoringResources).toList();
    }
    return AbstractMapper.super.map(entities, filter);
  }
}

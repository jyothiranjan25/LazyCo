package com.example.lazyco.entities.UserManagement.Role;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import com.example.lazyco.entities.UserManagement.RoleModuleResource.RoleModuleResourceMapper;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(
    componentModel = "spring",
    uses = {RoleModuleResourceMapper.class})
public interface RoleMapper extends AbstractMapper<RoleDTO, Role> {

  @Mapping(target = "roleModuleResources", ignore = true)
  RoleDTO map(Role entity);

  @Named("mapRoleModuleResource")
  RoleDTO mapRoleModuleResource(Role entity);

  @Override
  default List<RoleDTO> map(List<Role> entities, RoleDTO filter) {
    if (filter != null && Boolean.TRUE.equals(filter.getFetchModuleResources())) {
      return entities.stream().map(this::mapRoleModuleResource).toList();
    }
    return AbstractMapper.super.map(entities, filter);
  }
}

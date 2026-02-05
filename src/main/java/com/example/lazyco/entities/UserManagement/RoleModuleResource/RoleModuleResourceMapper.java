package com.example.lazyco.entities.UserManagement.RoleModuleResource;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import com.example.lazyco.entities.UserManagement.Module.ModuleMapper;
import com.example.lazyco.entities.UserManagement.Resource.ResourceMapper;
import com.example.lazyco.entities.UserManagement.Role.RoleMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = {RoleMapper.class, ModuleMapper.class, ResourceMapper.class})
public interface RoleModuleResourceMapper
    extends AbstractMapper<RoleModuleResourceDTO, RoleModuleResource> {

  @Mapping(target = "roleId", source = "role.id")
  @Mapping(target = "moduleId", source = "module.id")
  @Mapping(target = "resourceId", source = "resource.id")
  RoleModuleResourceDTO map(RoleModuleResource entity);
}

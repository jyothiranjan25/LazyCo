package com.example.lazyco.entities.UserManagement.Role;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper extends AbstractMapper<RoleDTO, Role> {}

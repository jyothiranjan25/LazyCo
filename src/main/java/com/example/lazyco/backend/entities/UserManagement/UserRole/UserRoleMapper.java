package com.example.lazyco.backend.entities.UserManagement.UserRole;

import com.example.lazyco.backend.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserRoleMapper extends AbstractMapper<UserRoleDTO, UserRole> {}

package com.example.lazyco.entities.UserManagement.UserRole;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import com.example.lazyco.entities.UserManagement.Role.RoleMapper;
import com.example.lazyco.entities.UserManagement.UserGroup.UserGroupMapper;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = {RoleMapper.class, UserGroupMapper.class})
public interface UserRoleMapper extends AbstractMapper<UserRoleDTO, UserRole> {

  @Mapping(target = "appUserId", source = "appUser.id")
  @Mapping(target = "roleId", source = "role.id")
  @Mapping(target = "userGroupId", source = "userGroup.id")
  UserRoleDTO map(UserRole entity);

  @Override
  default List<UserRoleDTO> map(List<UserRole> entities, UserRoleDTO filter) {
    return entities.stream().map(this::map).toList();
  }
}

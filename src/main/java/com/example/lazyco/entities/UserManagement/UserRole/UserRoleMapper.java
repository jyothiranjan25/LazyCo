package com.example.lazyco.entities.UserManagement.UserRole;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserRoleMapper extends AbstractMapper<UserRoleDTO, UserRole> {

  @Named("mapOnlyRole")
  @Mapping(target = "userGroup", ignore = true)
  UserRoleDTO mapOnlyRole(UserRole userRole);

  @Named("excludeRole")
  @Mapping(target = "role", ignore = true)
  UserRoleDTO excludeRole(UserRole userRole);

  @Override
  default List<UserRoleDTO> map(List<UserRole> entities, UserRoleDTO filter) {
    if (Boolean.TRUE.equals(filter.getFetchOnlyRole())) {
      return entities.stream().map(this::mapOnlyRole).toList();
    } else if (Boolean.FALSE.equals(filter.getFetchOnlyRole())) {
      return entities.stream().map(this::excludeRole).toList();
    }
    return entities.stream().map(this::map).toList();
  }
}

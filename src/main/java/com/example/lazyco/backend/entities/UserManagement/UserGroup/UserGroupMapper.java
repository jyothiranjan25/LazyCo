package com.example.lazyco.backend.entities.UserManagement.UserGroup;

import com.example.lazyco.backend.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserGroupMapper extends AbstractMapper<UserGroupDTO, UserGroup> {

  @Named("map")
  @Mapping(source = "parentUserGroup.id", target = "parentId")
  UserGroupDTO map(UserGroup entity);
}

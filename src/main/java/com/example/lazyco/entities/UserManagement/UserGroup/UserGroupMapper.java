package com.example.lazyco.entities.UserManagement.UserGroup;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserGroupMapper extends AbstractMapper<UserGroupDTO, UserGroup> {

  @Mapping(source = "parentUserGroup.id", target = "parentId")
  UserGroupDTO map(UserGroup entity);
}

package com.example.lazyco.entities.UserManagement.UserGroup;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import java.util.List;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserGroupMapper extends AbstractMapper<UserGroupDTO, UserGroup> {

  @Mapping(source = "parentUserGroup.id", target = "parentId")
  @Mapping(target = "childUserGroups", ignore = true)
  UserGroupDTO map(UserGroup entity);

  @Named("mapChildForParent")
  @Mapping(source = "parentUserGroup.id", target = "parentId")
  @Mapping(target = "childUserGroups", qualifiedByName = "mapChildForParent")
  UserGroupDTO mapChildForParent(UserGroup entity);

  @Override
  default List<UserGroupDTO> map(List<UserGroup> entities, UserGroupDTO filter) {
    if (Boolean.TRUE.equals(filter.getGetChildForParent())) {
      return entities.stream().map(this::mapChildForParent).toList();
    }
    return AbstractMapper.super.map(entities, filter);
  }
}

package com.example.lazyco.entities.UserManagement.UserGroup;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = UserGroup.class)
public class UserGroupDTO extends AbstractDTO<UserGroupDTO> {
  private String userGroupName;
  private String fullyQualifiedName;
  private String description;
  private Long parentId;
  private UserGroupDTO parentUserGroup;
  private List<UserGroupDTO> childUserGroups;
}

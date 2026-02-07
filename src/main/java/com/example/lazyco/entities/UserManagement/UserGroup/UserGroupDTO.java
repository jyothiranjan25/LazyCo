package com.example.lazyco.entities.UserManagement.UserGroup;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.FieldPath;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = UserGroup.class)
public class UserGroupDTO extends AbstractDTO<UserGroupDTO> {
  @InternalFilterableField private String userGroupName;
  private String fullyQualifiedName;
  private String description;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "parentUserGroup.id")
  private Long parentId;

  private Set<UserGroupDTO> childUserGroups;
  private Boolean fetchParent;
  private Boolean getChildForParent;
  private Boolean fetchForLoggedInUser;
}

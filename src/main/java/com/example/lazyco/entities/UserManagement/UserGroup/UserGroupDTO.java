package com.example.lazyco.entities.UserManagement.UserGroup;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.FieldPath;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = UserGroup.class)
public class UserGroupDTO extends AbstractDTO<UserGroupDTO> {
  @InternalFilterableField
  private String userGroupName;
  private String fullyQualifiedName;
  private String description;
  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "parentUserGroup.id")
  private Long parentId;
  private List<UserGroupDTO> childUserGroups;
  private Boolean fetchParent;
}
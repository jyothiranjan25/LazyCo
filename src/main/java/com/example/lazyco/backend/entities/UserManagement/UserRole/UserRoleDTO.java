package com.example.lazyco.backend.entities.UserManagement.UserRole;

import com.example.lazyco.backend.core.AbstractClasses.CriteriaBuilder.FieldFiltering.FieldPath;
import com.example.lazyco.backend.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.backend.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.entities.UserManagement.AppUser.AppUserDTO;
import com.example.lazyco.backend.entities.UserManagement.Role.RoleDTO;
import com.example.lazyco.backend.entities.UserManagement.UserGroup.UserGroupDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = UserRole.class)
public class UserRoleDTO extends AbstractDTO<UserRoleDTO> {

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "appUser.id")
  private Long appUserId;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "role.id")
  private Long roleId;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "userGroup.id")
  private Long userGroupId;

  private AppUserDTO appUser;

  private RoleDTO role;

  private UserGroupDTO userGroup;

  private Boolean fetchOnlyRole;
}

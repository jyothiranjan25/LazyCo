package com.example.lazyco.backend.entities.UserManagement.Role;

import com.example.lazyco.backend.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = Role.class)
public class RoleDTO extends AbstractDTO<RoleDTO> {
  private String name;
  private String description;
  private String icon;
  private RoleTypeEnum roleType;
}

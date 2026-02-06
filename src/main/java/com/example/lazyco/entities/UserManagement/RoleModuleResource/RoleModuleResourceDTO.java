package com.example.lazyco.entities.UserManagement.RoleModuleResource;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.FieldPath;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.entities.UserManagement.Module.ModuleDTO;
import com.example.lazyco.entities.UserManagement.Resource.ResourceDTO;
import com.example.lazyco.entities.UserManagement.Role.RoleDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = RoleModuleResource.class)
public class RoleModuleResourceDTO extends AbstractDTO<RoleModuleResourceDTO> {

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "role.id")
  private Long roleId;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "module.id")
  private Long moduleId;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "resource.id")
  private Long resourceId;

  private RoleDTO role;

  private ModuleDTO module;

  private ResourceDTO resource;

  private Integer displayOrder;
}

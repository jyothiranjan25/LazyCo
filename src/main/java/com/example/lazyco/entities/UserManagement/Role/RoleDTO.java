package com.example.lazyco.entities.UserManagement.Role;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.AbstractClasses.DTO.HasName;
import com.example.lazyco.entities.UserManagement.RoleModuleResource.RoleModuleResourceDTO;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = Role.class)
public class RoleDTO extends AbstractDTO<RoleDTO> implements HasName {
  @InternalFilterableField private String roleName;
  private String description;
  private String icon;
  private RoleTypeEnum roleType;
  private List<RoleModuleResourceDTO> roleModuleResources;
  private Boolean fetchModuleResources;

  @Override
  public String getName() {
    return roleName;
  }

  @Override
  public void setName(String name) {
    this.roleName = name;
  }
}

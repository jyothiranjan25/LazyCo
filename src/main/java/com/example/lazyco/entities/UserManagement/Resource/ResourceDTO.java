package com.example.lazyco.entities.UserManagement.Resource;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.FieldPath;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.AbstractClasses.DTO.HasName;
import jakarta.persistence.*;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = Resource.class)
public class ResourceDTO extends AbstractDTO<ResourceDTO> implements HasName {
  @InternalFilterableField private String resourceName;
  private String description;
  private Integer resourceOrder;
  private String action;
  private Boolean showInMenu;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "parentResource.id")
  private Long parentId;

  private List<ResourceDTO> childResources;
  private Boolean fetchParent;
  private Boolean getChildForParent;

  @Override
  public String getName() {
    return resourceName;
  }

  @Override
  public void setName(String name) {
    this.resourceName = name;
  }
}

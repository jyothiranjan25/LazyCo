package com.example.lazyco.entities.UserManagement.Resource;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.FieldPath;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import jakarta.persistence.*;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = Resource.class)
public class ResourceDTO extends AbstractDTO<ResourceDTO> {
  @InternalFilterableField private String resourceName;
  private String description;
  private Integer resourceOrder;
  private String action;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "parentResource.id")
  private Long parentId;

  private List<ResourceDTO> childResources;
  private Boolean fetchParent;
}

package com.example.lazyco.entities.UserManagement.Resource;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractModel;
import jakarta.persistence.*;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceDTO extends AbstractModel {

  private String resourceName;
  private String description;
  private Integer resourceOrder;
  private List<ResourceDTO> childrenResources;
}

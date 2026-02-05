package com.example.lazyco.entities.UserManagement.Module;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.entities.UserManagement.Resource.ResourceDTO;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = Module.class)
public class ModuleDTO extends AbstractDTO<ModuleDTO> {

  @InternalFilterableField private String moduleName;
  private String description;
  private String action;
  private List<ResourceDTO> addResources;
  private List<ResourceDTO> removeResources;
  private List<ResourceDTO> resources;
  private Boolean fetchResources;
}

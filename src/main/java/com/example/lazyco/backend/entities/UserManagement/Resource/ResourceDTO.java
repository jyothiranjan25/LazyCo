package com.example.lazyco.backend.entities.UserManagement.Resource;

import com.example.lazyco.backend.core.AbstractDocClasses.DTO.AbstractDTO;
import com.example.lazyco.backend.core.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.backend.core.MongoCriteriaBuilder.FilteredEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = Resource.class)
public class ResourceDTO extends AbstractDTO<ResourceDTO> {

  @InternalFilterableField private String name;
  @InternalFilterableField private String description;
  @InternalFilterableField private String url;
  private String type;
}

package com.example.lazyco.entities.TermSystem.TermMaster;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.FieldPath;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.AbstractClasses.DTO.HasCodeAndName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = TermMaster.class)
public class TermMasterDTO extends AbstractDTO<TermMasterDTO> implements HasCodeAndName {
  @InternalFilterableField private String code;
  @InternalFilterableField private String name;
  private String description;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "termSystem.id")
  private Long termSystemId;
}

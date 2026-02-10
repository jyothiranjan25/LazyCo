package com.example.lazyco.entities.TermCycle;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.FieldPath;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.AbstractClasses.DTO.HasCodeAndName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = TermCycle.class)
public class TermCycleDTO extends AbstractDTO<TermCycleDTO> implements HasCodeAndName {
  @InternalFilterableField private String code;
  @InternalFilterableField private String name;
  private String description;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "academicYear.id")
  private Long academicYearId;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "termMaster.id")
  private Long termMasterId;
}

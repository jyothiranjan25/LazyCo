package com.example.lazyco.entities.AcademicProgram.ProgramTermMaster;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.FieldPath;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.AbstractClasses.DTO.HasName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = ProgramTermMaster.class)
public class ProgramTermMasterDTO extends AbstractDTO<ProgramTermMasterDTO> implements HasName {
  @InternalFilterableField private String name;
  private String description;
  @InternalFilterableField private Integer termSequence;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "programTermSystem.id")
  private Long programTermSystemId;
}

package com.example.lazyco.entities.AcademicProgram;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.FieldPath;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.AbstractClasses.DTO.HasCodeAndName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = AcademicProgram.class)
public class AcademicProgramDTO extends AbstractDTO<AcademicProgramDTO> implements HasCodeAndName {
  @InternalFilterableField private String code;
  @InternalFilterableField private String name;
  private String description;
  private ProgramStudyModeEnum programStudyMode;
  private ProgramLevelEnum programLevel;
  @InternalFilterableField private Boolean isActive;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "institution.id")
  private Long institutionId;
}

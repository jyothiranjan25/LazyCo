package com.example.lazyco.entities.AcademicProgram.ProgramSpecialization;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.FieldPath;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.AbstractClasses.DTO.HasCodeAndName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = ProgramSpecialization.class)
public class ProgramSpecializationDTO extends AbstractDTO<ProgramSpecializationDTO>
    implements HasCodeAndName {
  @InternalFilterableField private String code;
  @InternalFilterableField private String name;
  private String description;
  @InternalFilterableField private Boolean isActive;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "academicProgram.id")
  private Long academicProgramId;
}

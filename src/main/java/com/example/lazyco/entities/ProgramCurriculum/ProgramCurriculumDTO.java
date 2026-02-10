package com.example.lazyco.entities.ProgramCurriculum;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.FieldPath;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.AbstractClasses.DTO.HasCodeAndName;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = ProgramCurriculum.class)
public class ProgramCurriculumDTO extends AbstractDTO<ProgramCurriculumDTO>
    implements HasCodeAndName {

  @InternalFilterableField private String code;
  @InternalFilterableField private String name;
  private String description;
  @InternalFilterableField private LocalDate startDate;
  @InternalFilterableField private LocalDate endDate;
  @InternalFilterableField private LocalDate convictionDate;
  @InternalFilterableField private Integer admissionCapacity;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "academicYear.id")
  private Long academicYearId;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "termSystem.id")
  private Long termSystemId;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "academicProgram.id")
  private Long academicProgramId;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "programTermSystem.id")
  private Long programTermSystemId;
}

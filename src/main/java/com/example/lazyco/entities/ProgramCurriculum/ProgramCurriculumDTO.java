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
  @InternalFilterableField private Integer admissionCapacity;

  private Integer minCredit;
  private Integer maxCredit;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "academicYear.id")
  private Long academicYearId;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "academicYear.code")
  private String academicYearCode;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "academicYear.name")
  private String academicYearName;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "termSystem.id")
  private Long termSystemId;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "termSystem.code")
  private String termSystemCode;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "termSystem.name")
  private String termSystemName;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "academicProgram.id")
  private Long academicProgramId;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "academicProgram.code")
  private String academicProgramCode;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "academicProgram.name")
  private String academicProgramName;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "programTermSystem.id")
  private Long programTermSystemId;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "programTermSystem.code")
  private String programTermSystemCode;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "programTermSystem.name")
  private String programTermSystemName;

  private ProgramCurriculumStatusEnum status;
}

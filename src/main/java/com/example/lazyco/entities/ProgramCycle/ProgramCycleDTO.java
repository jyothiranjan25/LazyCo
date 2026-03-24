package com.example.lazyco.entities.ProgramCycle;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.FieldPath;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.AbstractClasses.DTO.HasCode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = ProgramCycle.class)
public class ProgramCycleDTO extends AbstractDTO<ProgramCycleDTO> implements HasCode {

  @InternalFilterableField private String code;
  private String description;
  @InternalFilterableField private LocalDate startDate;
  @InternalFilterableField private LocalDate endDate;
  private Integer minCredit;
  private Integer maxCredit;
  @InternalFilterableField private LocalDateTime registrationStartDate;
  @InternalFilterableField private LocalDateTime registrationEndDate;
  @InternalFilterableField private LocalDateTime withdrawalDeadline;
  @InternalFilterableField private Boolean disableStudentRegistration;
  private Integer minRegistrationCredit;
  private Integer maxRegistrationCredit;
  @InternalFilterableField private LocalDateTime gradeSubmissionDeadline;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "programCurriculum.id")
  private Long programCurriculumId;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "programCurriculum.code")
  private String programCurriculumCode;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "programCurriculum.name")
  private String programCurriculumName;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "termCycle.id")
  private Long termCycleId;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "termCycle.code")
  private String termCycleCode;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "termCycle.name")
  private String termCycleName;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "programTermMaster.id")
  private Long programTermMasterId;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "programTermMaster.name")
  private String programTermMasterName;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "syllabusMaster.id")
  private Long SyllabusMasterId;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "syllabusMaster.code")
  private String SyllabusMasterCode;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "syllabusMaster.name")
  private String SyllabusMasterName;
}

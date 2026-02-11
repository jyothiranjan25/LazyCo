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
  @FieldPath(fullyQualifiedPath = "termCycle.id")
  private Long termCycleId;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "programTermMaster.id")
  private Long programTermMasterId;
}

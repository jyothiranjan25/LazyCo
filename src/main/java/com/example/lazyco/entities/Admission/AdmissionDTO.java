package com.example.lazyco.entities.Admission;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.FieldPath;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = Admission.class)
public class AdmissionDTO extends AbstractDTO<AdmissionDTO> {

  @InternalFilterableField private String admissionNumber;
  @InternalFilterableField private String universityNumber;
  @InternalFilterableField private LocalDateTime admissionDate;
  @InternalFilterableField private String officialEmail;
  @InternalFilterableField private AdmissionStatusEnum admissionStatus;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "applicationForm.id")
  private Long applicationFormId;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "programCurriculum.id")
  private Long programCurriculumId;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "joiningProgramCycle.id")
  private Long joiningProgramCycleId;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "currentProgramCycle.id")
  private Long currentProgramCycleId;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "student.id")
  private Long studentId;
}

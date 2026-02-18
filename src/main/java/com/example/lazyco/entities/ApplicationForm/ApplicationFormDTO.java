package com.example.lazyco.entities.ApplicationForm;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.FieldPath;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.Utils.GenderEnum;
import com.google.gson.annotations.Expose;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = ApplicationForm.class)
public class ApplicationFormDTO extends AbstractDTO<ApplicationFormDTO> {

  @InternalFilterableField private String applicationNumber;

  private String firstName;

  private String middleName;

  private String lastName;

  private String fullName;

  @InternalFilterableField private GenderEnum gender;

  @InternalFilterableField private LocalDate dateOfBirth;

  @InternalFilterableField private String email;

  @InternalFilterableField private String phoneNumber;

  @InternalFilterableField private LocalDateTime applicationDate;

  @InternalFilterableField private String rawProgramName;

  private Boolean isEnrolled;

  @InternalFilterableField
  @Expose(deserialize = false)
  private ApplicationFormSourceEnum source;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "admissionOffer.id")
  private Long admissionOfferId;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "admissionOffer.code")
  private String admissionOfferCode;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "programCurriculum.id")
  private Long programCurriculumId;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "programCurriculum.code")
  private String programCurriculumCode;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "startingProgramCycle.id")
  private Long startingProgramCycleId;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "startingProgramCycle.code")
  private String startingProgramCycleCode;

  private Map<String, Object> customFields;
}

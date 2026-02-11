package com.example.lazyco.entities.AdmissionOffer.AdmissionOfferProgram;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.FieldPath;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = AdmissionOfferProgram.class)
public class AdmissionOfferProgramDTO extends AbstractDTO<AdmissionOfferProgramDTO> {

  @InternalFilterableField private String code;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "admissionOffer.id")
  private Long admissionOfferId;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "programCurriculum.id")
  private Long programCurriculumId;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "programCycle.id")
  private Long programCycleId;
}

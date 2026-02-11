package com.example.lazyco.entities.AdmissionOffer;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.FieldPath;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.AbstractClasses.DTO.HasCodeAndName;
import com.example.lazyco.entities.AdmissionOffer.AdmissionOfferProgram.AdmissionOfferProgramDTO;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = AdmissionOffer.class)
public class AdmissionOfferDTO extends AbstractDTO<AdmissionOfferDTO> implements HasCodeAndName {

  @InternalFilterableField private String code;
  @InternalFilterableField private String name;
  private String description;
  @InternalFilterableField private LocalDate startDate;
  @InternalFilterableField private LocalDate endDate;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "academicYear.id")
  private Long academicYearId;

  private List<AdmissionOfferProgramDTO> offerPrograms;
}

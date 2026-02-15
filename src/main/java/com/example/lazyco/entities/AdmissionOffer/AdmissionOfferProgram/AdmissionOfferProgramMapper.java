package com.example.lazyco.entities.AdmissionOffer.AdmissionOfferProgram;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AdmissionOfferProgramMapper
    extends AbstractMapper<AdmissionOfferProgramDTO, AdmissionOfferProgram> {

  @Mapping(target = "admissionOfferId", source = "admissionOffer.id")
  @Mapping(target = "programCurriculumId", source = "programCurriculum.id")
  @Mapping(target = "programCycleId", source = "programCycle.id")
  AdmissionOfferProgramDTO map(AdmissionOfferProgram entity);
}

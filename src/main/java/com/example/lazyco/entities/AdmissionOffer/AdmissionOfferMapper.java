package com.example.lazyco.entities.AdmissionOffer;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AdmissionOfferMapper extends AbstractMapper<AdmissionOfferDTO, AdmissionOffer> {

  @Mapping(target = "academicYearId", source = "academicYear.id")
  @Mapping(target = "applicationFormTemplateId", source = "applicationFormTemplate.id")
  AdmissionOfferDTO map(AdmissionOffer entity);
}

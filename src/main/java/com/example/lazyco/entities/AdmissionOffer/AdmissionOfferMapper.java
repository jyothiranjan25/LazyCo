package com.example.lazyco.entities.AdmissionOffer;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AdmissionOfferMapper extends AbstractMapper<AdmissionOfferDTO, AdmissionOffer> {

  @Mapping(target = "academicYearId", source = "academicYear.id")
  @Mapping(target = "academicYearCode", source = "academicYear.code")
  @Mapping(target = "academicYearName", source = "academicYear.name")
  @Mapping(target = "applicationFormTemplateId", source = "applicationFormTemplate.id")
  @Mapping(target = "applicationFormTemplateCode", source = "applicationFormTemplate.code")
  @Mapping(target = "applicationFormTemplateName", source = "applicationFormTemplate.name")
  AdmissionOfferDTO map(AdmissionOffer entity);
}

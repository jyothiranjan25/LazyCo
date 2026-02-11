package com.example.lazyco.entities.AdmissionOffer.AdmissionOfferProgram;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdmissionOfferProgramMapper
    extends AbstractMapper<AdmissionOfferProgramDTO, AdmissionOfferProgram> {}

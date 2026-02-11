package com.example.lazyco.entities.AdmissionOffer;

import com.example.lazyco.core.AbstractClasses.Mapper.AbstractMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdmissionOfferMapper extends AbstractMapper<AdmissionOfferDTO, AdmissionOffer> {}

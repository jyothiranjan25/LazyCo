package com.example.lazyco.entities.Admission;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = Admission.class)
public class AdmissionDTO extends AbstractDTO<AdmissionDTO> {}

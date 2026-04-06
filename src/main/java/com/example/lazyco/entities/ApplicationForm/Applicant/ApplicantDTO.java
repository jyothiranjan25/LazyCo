package com.example.lazyco.entities.ApplicationForm.Applicant;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = Applicant.class)
public class ApplicantDTO extends AbstractDTO<ApplicantDTO> {}

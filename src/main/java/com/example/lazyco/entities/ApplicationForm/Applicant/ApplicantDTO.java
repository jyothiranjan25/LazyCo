package com.example.lazyco.entities.ApplicationForm.Applicant;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.FieldPath;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = Applicant.class)
public class ApplicantDTO extends AbstractDTO<ApplicantDTO> {

  @InternalFilterableField private String email;
  @InternalFilterableField private String phoneNumber;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "applicationForms.id")
  private List<Long> applicationIds;
}

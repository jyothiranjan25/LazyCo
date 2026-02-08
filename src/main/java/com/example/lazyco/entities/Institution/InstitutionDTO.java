package com.example.lazyco.entities.Institution;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.FieldPath;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.AbstractClasses.DTO.HasCodeAndName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = Institution.class)
public class InstitutionDTO extends AbstractDTO<InstitutionDTO> implements HasCodeAndName {
  @InternalFilterableField private String code;
  @InternalFilterableField private String name;
  private String description;
  @InternalFilterableField private Boolean isActive;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "university.id")
  private Long universityId;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "university.code")
  private String universityCode;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "university.name")
  private String universityName;
}

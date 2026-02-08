package com.example.lazyco.entities.University;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.AbstractClasses.DTO.HasCodeAndName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = University.class)
public class UniversityDTO extends AbstractDTO<UniversityDTO> implements HasCodeAndName {
  @InternalFilterableField private String code;
  @InternalFilterableField private String name;
  private String description;
  private Boolean isActive;
}

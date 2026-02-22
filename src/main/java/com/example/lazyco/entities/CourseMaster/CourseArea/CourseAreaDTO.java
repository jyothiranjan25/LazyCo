package com.example.lazyco.entities.CourseMaster.CourseArea;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.AbstractClasses.DTO.HasName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = CourseArea.class)
public class CourseAreaDTO extends AbstractDTO<CourseAreaDTO> implements HasName {
  @InternalFilterableField private String name;
  private String description;
}

package com.example.lazyco.entities.SyllabusMaster.CourseCategory;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.AbstractClasses.DTO.HasName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = CourseCategory.class)
public class CourseCategoryDTO extends AbstractDTO<CourseCategoryDTO> implements HasName {
  @InternalFilterableField private String name;
  private String description;
}

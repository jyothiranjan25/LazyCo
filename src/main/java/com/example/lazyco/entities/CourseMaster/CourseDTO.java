package com.example.lazyco.entities.CourseMaster;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.FieldPath;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.core.AbstractClasses.DTO.HasCodeAndName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = Course.class)
public class CourseDTO extends AbstractDTO<CourseDTO> implements HasCodeAndName {

  @InternalFilterableField private String code;
  @InternalFilterableField private String name;
  private String description;
  private String courseAim;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "courseArea.id")
  private Long courseAreaId;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "courseArea.name")
  private String courseAreaName;

  private String courseAreaDescription;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "institution.id")
  private Long institutionId;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "institution.code")
  private String institutionCode;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "institution.name")
  private String institutionName;
}

package com.example.lazyco.entities.CourseMaster.CourseClassType;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.FieldPath;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = CourseClassType.class)
public class CourseClassTypeDTO extends AbstractDTO<CourseClassTypeDTO> {

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "course.id")
  private Long courseId;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "course.code")
  private String courseCode;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "course.name")
  private String courseName;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "classType.id")
  private Long classTypeId;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "classType.name")
  private String classTypeName;
}

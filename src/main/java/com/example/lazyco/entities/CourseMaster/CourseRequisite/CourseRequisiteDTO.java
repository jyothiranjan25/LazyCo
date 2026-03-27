package com.example.lazyco.entities.CourseMaster.CourseRequisite;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.FieldPath;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = CourseRequisite.class)
public class CourseRequisiteDTO extends AbstractDTO<CourseRequisiteDTO> {

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
  @FieldPath(fullyQualifiedPath = "requisiteCourse.id")
  private Long requisiteCourseId;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "requisiteCourse.code")
  private String requisiteCourseCode;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "requisiteCourse.name")
  private String requisiteCourseName;

  @InternalFilterableField private CourseRequisiteTypeEnum requisiteType;
}

package com.example.lazyco.entities.SyllabusMaster.SyllabusOfferedCourse;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.FieldPath;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = SyllabusOfferedCourse.class)
public class SyllabusOfferedCourseDTO extends AbstractDTO<SyllabusOfferedCourseDTO> {

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "syllabusCourse.id")
  private Long syllabusCourseId;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "courseClassType.id")
  private Long courseClassTypeId;

  @InternalFilterableField private Double credit;

  @InternalFilterableField private Boolean isMandatory;
}

package com.example.lazyco.entities.SyllabusMaster.SyllabusCourse;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.FieldPath;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = SyllabusCourse.class)
public class SyllabusCourseDTO extends AbstractDTO<SyllabusCourseDTO> {

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "syllabusMaster.id")
  private Long syllabusMasterId;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "courseCategory.id")
  private Long courseCategoryId;

  @InternalFilterableField
  @FieldPath(fullyQualifiedPath = "courseCredit.id")
  private Long courseCreditId;
}

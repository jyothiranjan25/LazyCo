package com.example.lazyco.entities.CourseMaster.CourseCredit;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.FieldPath;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.entities.CourseMaster.CourseClassType.CourseClassType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = CourseCredit.class)
public class CourseCreditDTO extends AbstractDTO<CourseCreditDTO> {

    @InternalFilterableField
    @FieldPath(fullyQualifiedPath = "course.id")
    private Long courseId;

    @InternalFilterableField
    private Double credit;

    @InternalFilterableField
    private Boolean allowRollOver;

    @InternalFilterableField
    private Integer termSpan;
}

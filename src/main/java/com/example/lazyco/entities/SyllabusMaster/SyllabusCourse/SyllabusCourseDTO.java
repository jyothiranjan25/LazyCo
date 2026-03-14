package com.example.lazyco.entities.SyllabusMaster.SyllabusCourse;

import com.example.lazyco.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.core.AbstractClasses.DTO.AbstractDTO;
import com.example.lazyco.entities.CourseMaster.CourseCredit.CourseCredit;
import com.example.lazyco.entities.SyllabusMaster.CourseCategory.CourseCategory;
import com.example.lazyco.entities.SyllabusMaster.SyllabusMaster;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = SyllabusCourse.class)
public class SyllabusCourseDTO extends AbstractDTO<SyllabusCourseDTO> {

    private Long syllabusMasterId;

    private Long courseCategoryId;

    private Long courseCreditId;
}

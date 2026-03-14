package com.example.lazyco.entities.CourseMaster.CourseRequisite;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CourseRequisiteService
    extends CommonAbstractService<CourseRequisiteDTO, CourseRequisite> {
  protected CourseRequisiteService(CourseRequisiteMapper courseRequisiteMapper) {
    super(courseRequisiteMapper);
  }

  public CourseRequisiteDTO create(CourseRequisiteDTO dto) {
    if (dto.getRequisiteType() == CourseRequisiteTypeEnum.COREQUISITE
        || dto.getRequisiteType() == CourseRequisiteTypeEnum.ANTIREQUISITE) {

      List<CourseRequisiteDTO> bidirectionalRequisites = new ArrayList<>();

      // For Course A
      CourseRequisiteDTO bidirectionalRequisite = new CourseRequisiteDTO();
      bidirectionalRequisite.setCourseId(dto.getCourseId());
      bidirectionalRequisite.setRequisiteCourseId(dto.getRequisiteCourseId());
      bidirectionalRequisite.setRequisiteType(dto.getRequisiteType());
      bidirectionalRequisites.add(bidirectionalRequisite);

      // For Course B
      bidirectionalRequisite = new CourseRequisiteDTO();
      bidirectionalRequisite.setCourseId(dto.getRequisiteCourseId());
      bidirectionalRequisite.setRequisiteCourseId(dto.getCourseId());
      bidirectionalRequisite.setRequisiteType(dto.getRequisiteType());
      bidirectionalRequisites.add(bidirectionalRequisite);

      dto.setObjects(bidirectionalRequisites);
    }
    return super.create(dto);
  }

  public CourseRequisiteDTO delete(CourseRequisiteDTO dto) {
    return super.delete(dto);
  }
}

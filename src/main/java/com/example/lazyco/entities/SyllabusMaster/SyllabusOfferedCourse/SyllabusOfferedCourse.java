package com.example.lazyco.entities.SyllabusMaster.SyllabusOfferedCourse;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractRBACModel;
import com.example.lazyco.entities.CourseMaster.CourseClassType.CourseClassType;
import com.example.lazyco.entities.SyllabusMaster.SyllabusCourse.SyllabusCourse;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.Audited;

@Getter
@Setter
@Audited
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "syllabus_offered_course", comment = "Table to store syllabus offered course details",
    indexes = {
        @Index(name = "idx_syllabus_offered_course_syllabus_course_id", columnList = "syllabus_course_id"),
        @Index(name = "idx_syllabus_offered_course_course_class_type_id", columnList = "course_class_type_id")
    })
@EntityListeners(SyllabusOfferedCourseListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SyllabusOfferedCourse extends AbstractRBACModel {

  @ManyToOne
  @JoinColumn(
      name = "syllabus_course_id",
      foreignKey = @ForeignKey(name = "fk_syllabus_offered_course_syllabus_course_id"),
      nullable = false,
      comment = "Reference to the syllabus course")
  private SyllabusCourse syllabusCourse;

  @ManyToOne
  @JoinColumn(
      name = "course_class_type_id",
      foreignKey = @ForeignKey(name = "fk_syllabus_offered_course_course_class_type_id"),
      nullable = false,
      comment = "Reference to the course class type")
  private CourseClassType courseClassType;

  @Column(name = "credit", nullable = false, comment = "credit for the course")
  private Double credit;

  @Column(
      name = "is_mandatory",
      columnDefinition = "boolean default false",
      comment = "credit for the course")
  private Boolean isMandatory;
}

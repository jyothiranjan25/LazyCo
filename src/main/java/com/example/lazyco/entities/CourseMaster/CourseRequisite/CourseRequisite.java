package com.example.lazyco.entities.CourseMaster.CourseRequisite;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractRBACModel;
import com.example.lazyco.entities.CourseMaster.Course;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import org.hibernate.envers.Audited;

@Getter
@Setter
@Audited
@Entity
@DynamicUpdate
@DynamicInsert
@Table(
    name = "course_requisite",
    comment = "table to store course requisite information",
    indexes = {
      @Index(name = "idx_course_requisite_course_id", columnList = "course_id"),
      @Index(name = "idx_course_requisite_requisite_course_id", columnList = "requisite_course_id")
    },
    uniqueConstraints = {
      @UniqueConstraint(
          name = "uk_course_requisite_course_requisite_course",
          columnNames = {"course_id", "requisite_course_id", "requisite_type"})
    })
@EntityListeners(CourseRequisiteListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CourseRequisite extends AbstractRBACModel {

  @ManyToOne
  @JoinColumn(
      name = "course_id",
      foreignKey = @ForeignKey(name = "fk_course_requisite_course"),
      nullable = false,
      comment = "Reference to the course that has the requisite")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Course course;

  @ManyToOne
  @JoinColumn(
      name = "requisite_course_id",
      foreignKey = @ForeignKey(name = "fk_course_requisite_requisite_course"),
      nullable = false,
      comment = "Reference to the course that is the requisite")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Course requisiteCourse;

  @Column(
      name = "requisite_type",
      nullable = false,
      comment = "Type of requisite (e.g., Prerequisite, Corequisite, etc.)")
  @Enumerated(EnumType.STRING)
  private CourseRequisiteTypeEnum requisiteType;
}

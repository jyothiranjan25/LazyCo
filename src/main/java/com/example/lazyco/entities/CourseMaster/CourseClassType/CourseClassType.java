package com.example.lazyco.entities.CourseMaster.CourseClassType;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractRBACModel;
import com.example.lazyco.entities.CourseMaster.ClassType.ClassType;
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
    name = "course_class_type",
    comment = "table to store course class type information",
    indexes = {
      @Index(name = "idx_course_class_type_course_id", columnList = "course_id"),
      @Index(name = "idx_course_class_type_class_type_id", columnList = "class_type_id"),
      @Index(
          name = "idx_course_class_type_course_class_type",
          columnList = "course_id, class_type_id")
    },
    uniqueConstraints = {
      @UniqueConstraint(
          name = "uk_course_class_type_course_class_type",
          columnNames = {"course_id", "class_type_id"})
    })
@EntityListeners(CourseClassTypeListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CourseClassType extends AbstractRBACModel {

  @ManyToOne
  @JoinColumn(
      name = "course_id",
      foreignKey = @ForeignKey(name = "fk_course_class_type_course"),
      nullable = false,
      comment = "Reference to the course")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Course course;

  @ManyToOne
  @JoinColumn(
      name = "class_type_id",
      foreignKey = @ForeignKey(name = "fk_course_class_type_class_type"),
      nullable = false,
      comment = "Reference to the class type")
  @OnDelete(action = OnDeleteAction.RESTRICT)
  private ClassType classType;
}

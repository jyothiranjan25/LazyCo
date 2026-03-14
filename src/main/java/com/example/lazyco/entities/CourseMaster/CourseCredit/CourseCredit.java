package com.example.lazyco.entities.CourseMaster.CourseCredit;

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
    name = "course_credit",
    comment = "table to store course credit information",
    indexes = {@Index(name = "idx_course_credit_course_id", columnList = "course_id")})
@EntityListeners(CourseCreditListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CourseCredit extends AbstractRBACModel {

  @ManyToOne
  @JoinColumn(
      name = "course_id",
      foreignKey = @ForeignKey(name = "fk_course_credit_course"),
      nullable = false,
      comment = "Reference to the course that has the credit information")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Course course;

  @Column(name = "credit", nullable = false, comment = "number of credits for the course")
  private Double credit;

  @Column(
      name = "allow_roll_over",
      nullable = false,
      columnDefinition = "boolean default false",
      comment = "indicates whether the credit can be rolled over to the next semester")
  private Boolean allowRollOver;

  @Column(
      name = "term_span",
      nullable = false,
      columnDefinition = "integer default 1",
      comment =
          "number of terms the credit is valid for (e.g., 1 for one semester, 2 for one year)")
  private Integer termSpan;
}

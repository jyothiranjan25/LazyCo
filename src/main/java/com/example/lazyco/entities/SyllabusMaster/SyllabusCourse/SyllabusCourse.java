package com.example.lazyco.entities.SyllabusMaster.SyllabusCourse;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractRBACModel;
import com.example.lazyco.entities.CourseMaster.CourseCredit.CourseCredit;
import com.example.lazyco.entities.SyllabusMaster.CourseCategory.CourseCategory;
import com.example.lazyco.entities.SyllabusMaster.SyllabusMaster;
import com.example.lazyco.entities.SyllabusMaster.SyllabusOfferedCourse.SyllabusOfferedCourse;
import jakarta.persistence.*;
import java.util.Set;
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
@Table(name = "syllabus_course", comment = "Table to store syllabus course details",
    indexes = {
        @Index(name = "idx_syllabus_course_syllabus_master_id", columnList = "syllabus_master_id"),
        @Index(name = "idx_syllabus_course_course_category_id", columnList = "course_category_id"),
        @Index(name = "idx_syllabus_course_course_credit_id", columnList = "course_credit_id")
    })
@EntityListeners(SyllabusCourseListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SyllabusCourse extends AbstractRBACModel {

  @ManyToOne
  @JoinColumn(
      name = "syllabus_master_id",
      foreignKey = @ForeignKey(name = "fk_syllabus_course_syllabus_master_id"),
      nullable = false,
      comment = "Reference to the syllabus master")
  private SyllabusMaster syllabusMaster;

  @ManyToOne
  @JoinColumn(
      name = "course_category_id",
      foreignKey = @ForeignKey(name = "fk_syllabus_course_course_category_id"),
      nullable = false,
      comment = "Reference to the course category")
  private CourseCategory courseCategory;

  @ManyToOne
  @JoinColumn(
      name = "course_credit_id",
      foreignKey = @ForeignKey(name = "fk_syllabus_course_course_credit_id"),
      nullable = false,
      comment = "Reference to the course credit")
  private CourseCredit courseCredit;

  @OneToMany(mappedBy = "syllabusCourse", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<SyllabusOfferedCourse> syllabusOfferedCourse;
}
package com.example.lazyco.entities.CourseMaster;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractRBACModel;
import com.example.lazyco.entities.CourseMaster.CourseArea.CourseArea;
import com.example.lazyco.entities.CourseMaster.CourseClassType.CourseClassType;
import com.example.lazyco.entities.CourseMaster.CourseCredit.CourseCredit;
import com.example.lazyco.entities.Institution.Institution;
import jakarta.persistence.*;
import java.util.Set;
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
    name = "course",
    comment = "table to store course information",
    indexes = {
      @Index(name = "idx_course_code", columnList = "code"),
      @Index(name = "idx_course_name", columnList = "name")
    },
    uniqueConstraints = {@UniqueConstraint(name = "uk_course_code", columnNames = "code")})
@EntityListeners(CourseListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Course extends AbstractRBACModel {

  @Column(name = "code", nullable = false, comment = "Unique code for the course")
  private String code;

  @Column(name = "name", nullable = false, comment = "Name of the course")
  private String name;

  @Column(name = "description", comment = "Detailed description of the course")
  private String description;

  @Column(name = "course_aim", length = 512, comment = "Aim or objective of the course")
  private String courseAim;

  @ManyToOne
  @JoinColumn(
      name = "course_area_id",
      foreignKey = @ForeignKey(name = "fk_course_course_area"),
      nullable = false,
      comment = "Reference to the course area this course belongs to")
  private CourseArea courseArea;

  @ManyToOne
  @JoinColumn(
      name = "institution_id",
      foreignKey = @ForeignKey(name = "fk_course_institution"),
      nullable = false,
      comment = "Reference to the institution offering this course")
  private Institution institution;

  @OneToMany(mappedBy = "course")
  private Set<CourseClassType> courseClassTypes;

  @OneToMany(mappedBy = "course")
  private Set<CourseCredit> courseCredits;
}

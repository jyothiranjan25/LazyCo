package com.example.lazyco.entities.CourseMaster.CourseArea;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractRBACModel;
import com.example.lazyco.entities.CourseMaster.Course;
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
@Table(
    name = "course_area",
    comment = "table to store course area information",
    indexes = {@Index(name = "idx_course_area_name", columnList = "name")})
@EntityListeners(CourseAreaListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CourseArea extends AbstractRBACModel {

  @Column(name = "name", nullable = false, comment = "name of the course area")
  private String name;

  @Column(name = "description", comment = "description of the course area")
  private String description;

  @OneToMany(mappedBy = "courseArea")
  private Set<Course> courses;
}

package com.example.lazyco.entities.SyllabusMaster.CourseCategory;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractModel;
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
@Table(
    name = "course_category",
    comment = "table to store course category information",
    indexes = {@Index(name = "idx_course_category_name", columnList = "name")})
@EntityListeners(CourseCategoryListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CourseCategory extends AbstractModel {

  @Column(name = "name", nullable = false, comment = "name of the course category")
  private String name;

  @Column(name = "description", comment = "description of the course category")
  private String description;

  // e.g. "Core", "Elective", "General Education", etc.
}

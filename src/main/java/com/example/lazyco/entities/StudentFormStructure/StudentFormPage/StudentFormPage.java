package com.example.lazyco.entities.StudentFormStructure.StudentFormPage;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractRBACModel;
import com.example.lazyco.entities.StudentFormStructure.StudentFormPageSection.StudentFormPageSection;
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
    name = "student_form_page",
    comment = "Table storing student form page details",
    indexes = {
      @Index(name = "idx_student_form_page_name", columnList = "name"),
      @Index(name = "idx_student_form_page_key", columnList = "key"),
    })
@EntityListeners(StudentFormPageListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class StudentFormPage extends AbstractRBACModel {

  @Column(name = "name", comment = "Name of the student form page")
  private String name;

  @Column(name = "key", comment = "Unique key for the student form page")
  private String key;

  @Column(name = "description", comment = "Description of the student form page")
  private String description;

  @Column(
      name = "display_order",
      columnDefinition = "integer default 0",
      comment = "Order of the page in the student form")
  private Integer order;

  @OneToMany(mappedBy = "studentFormPage")
  private Set<StudentFormPageSection> studentFormPageSections;
}

package com.example.lazyco.entities.StudentFormStructure.StudentFormPageSection;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractRBACModel;
import com.example.lazyco.entities.StudentFormStructure.StudentFormPage.StudentFormPage;
import com.example.lazyco.entities.StudentFormStructure.StudentFormSectionCustomField.StudentFormSectionCustomField;
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
    name = "student_form_page_section",
    comment = "Table storing student form page section details",
    indexes = {
      @Index(name = "idx_student_form_page_section_name", columnList = "name"),
      @Index(name = "idx_student_form_page_section_key", columnList = "key"),
      @Index(name = "idx_student_form_page_section_page_id", columnList = "student_form_page_id")
    })
@EntityListeners(StudentFormPageSectionListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class StudentFormPageSection extends AbstractRBACModel {

  @Column(name = "name", comment = "Name of the student form section")
  private String name;

  @Column(name = "key", comment = "Unique key for the student form section")
  private String key;

  @Column(name = "description", comment = "Description of the student form section")
  private String description;

  @Column(
      name = "display_order",
      columnDefinition = "integer default 0",
      comment = "Order of the section within the student form page")
  private Integer order;

  @ManyToOne
  @JoinColumn(
      name = "student_form_page_id",
      foreignKey = @ForeignKey(name = "fk_student_form_page_section_page_id"),
      nullable = false,
      comment = "Reference to the student form page this section belongs to")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private StudentFormPage studentFormPage;

  @OneToMany(mappedBy = "studentFormPageSection")
  private Set<StudentFormSectionCustomField> studentFormSectionCustomFields;
}

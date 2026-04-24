package com.example.lazyco.entities.StudentFormStructure.StudentFormSectionCustomField;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractRBACModel;
import com.example.lazyco.entities.CustomField.CustomField;
import com.example.lazyco.entities.StudentFormStructure.StudentFormPageSection.StudentFormPageSection;
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
    name = "student_form_section_custom_field",
    comment = "Table storing custom fields for student form sections",
    indexes = {
      @Index(
          name = "idx_student_form_section_custom_field_page_section_id",
          columnList = "student_form_page_section_id"),
      @Index(
          name = "idx_student_form_section_custom_field_custom_field_id",
          columnList = "custom_field_id")
    })
@EntityListeners(StudentFormSectionCustomFieldListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class StudentFormSectionCustomField extends AbstractRBACModel {

  @Column(
      name = "display_order",
      columnDefinition = "integer default 0",
      comment = "Order of the custom field within the student form section")
  private Integer order;

  @ManyToOne
  @JoinColumn(
      name = "student_form_page_section_id",
      foreignKey = @ForeignKey(name = "fk_student_form_section_custom_field_page_section_id"),
      nullable = false,
      comment = "Reference to the student form page section this custom field belongs to")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private StudentFormPageSection studentFormPageSection;

  @ManyToOne
  @JoinColumn(
      name = "custom_field_id",
      foreignKey = @ForeignKey(name = "fk_student_form_section_custom_field_custom_field_id"),
      nullable = false,
      comment =
          "Reference to the custom field associated with this student form section custom field")
  @OnDelete(action = OnDeleteAction.RESTRICT)
  private CustomField customField;
}

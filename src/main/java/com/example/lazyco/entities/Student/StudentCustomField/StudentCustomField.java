package com.example.lazyco.entities.Student.StudentCustomField;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractRBACModel;
import com.example.lazyco.entities.CustomField.CustomField;
import com.example.lazyco.entities.Student.Student;
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
    name = "student_custom_field",
    comment = "Table storing custom fields for student",
    indexes = {
      @Index(name = "idx_student_form_custom_field_student_form_id", columnList = "student_id"),
      @Index(name = "idx_student_form_custom_field_custom_field_id", columnList = "custom_field_id")
    },
    uniqueConstraints = {
      @UniqueConstraint(
          name = "uk_student_form_custom_field_aform_id_cfield_id",
          columnNames = {"student_id", "custom_field_id"})
    })
@EntityListeners(StudentCustomFieldListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class StudentCustomField extends AbstractRBACModel {

  @ManyToOne
  @JoinColumn(
      name = "student_id",
      foreignKey = @ForeignKey(name = "fk_student_form_cfield_aform_id"),
      nullable = false,
      comment = "Reference to the student form this custom field belongs to")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Student student;

  @ManyToOne
  @JoinColumn(
      name = "custom_field_id",
      foreignKey = @ForeignKey(name = "fk_student_form_cfield_cfield_id"),
      nullable = false,
      comment = "Reference to the custom field definition")
  @OnDelete(action = OnDeleteAction.RESTRICT)
  private CustomField customField;

  @Column(name = "value", comment = "Value of the custom field for this student")
  private String value;
}

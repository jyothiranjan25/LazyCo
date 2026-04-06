package com.example.lazyco.entities.ApplicationForm.ApplicationFormCustomField;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractRBACModel;
import com.example.lazyco.entities.ApplicationForm.ApplicationForm;
import com.example.lazyco.entities.CustomField.CustomField;
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
    name = "application_form_custom_field",
    comment = "Table storing custom fields for application forms",
    indexes = {
      @Index(
          name = "idx_application_form_custom_field_application_form_id",
          columnList = "application_form_id"),
      @Index(
          name = "idx_application_form_custom_field_custom_field_id",
          columnList = "custom_field_id")
    },
    uniqueConstraints = {
      @UniqueConstraint(
          name = "uk_application_form_custom_field_application_form_id_custom_field_id",
          columnNames = {"application_form_id", "custom_field_id"})
    })
@EntityListeners(ApplicationFormCustomFieldListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ApplicationFormCustomField extends AbstractRBACModel {

  @ManyToOne
  @JoinColumn(
      name = "application_form_id",
      foreignKey = @ForeignKey(name = "fk_application_form_custom_field_application_form_id"),
      nullable = false,
      comment = "Reference to the application form this custom field belongs to")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private ApplicationForm applicationForm;

  @ManyToOne
  @JoinColumn(
      name = "custom_field_id",
      foreignKey = @ForeignKey(name = "fk_application_form_custom_field_custom_field_id"),
      nullable = false,
      comment = "Reference to the custom field definition")
  @OnDelete(action = OnDeleteAction.RESTRICT)
  private CustomField customField;

  @Column(name = "value", comment = "Value of the custom field for this application form")
  private String value;
}

package com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormSectionCustomField;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractRBACModel;
import com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormPageSection.ApplicationFormPageSection;
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
    name = "application_form_section_custom_field",
    comment = "Table storing custom fields for application form sections",
    indexes = {
      @Index(name = "idx_application_form_section_custom_field_name", columnList = "name"),
      @Index(name = "idx_application_form_section_custom_field_key", columnList = "key"),
      @Index(
          name = "idx_application_form_section_custom_field_page_section_id",
          columnList = "application_form_page_section_id")
    })
@EntityListeners(ApplicationFormSectionCustomFieldListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ApplicationFormSectionCustomField extends AbstractRBACModel {

  @Column(name = "name", comment = "Name of the application form section custom field")
  private String name;

  @Column(name = "key", comment = "Unique key for the application form section custom field")
  private String key;

  @Column(
      name = "is_required",
      columnDefinition = "boolean default false",
      comment = "Indicates whether the custom field is required")
  private Boolean isRequired;

  @ManyToOne
  @JoinColumn(
      name = "application_form_page_section_id",
      foreignKey = @ForeignKey(name = "fk_application_form_section_custom_field_page_section_id"),
      nullable = false,
      comment = "Reference to the application form page section this custom field belongs to")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private ApplicationFormPageSection applicationFormPageSection;
}

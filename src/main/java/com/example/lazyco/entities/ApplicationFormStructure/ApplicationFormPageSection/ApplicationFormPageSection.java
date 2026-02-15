package com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormPageSection;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractRBACModel;
import com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormPage.ApplicationFormPage;
import com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormSectionCustomField.ApplicationFormSectionCustomField;
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
    name = "application_form_page_section",
    comment = "Table storing application form page section details",
    indexes = {
      @Index(name = "idx_application_form_page_section_name", columnList = "name"),
      @Index(name = "idx_application_form_page_section_key", columnList = "key"),
      @Index(
          name = "idx_application_form_page_section_page_id",
          columnList = "application_form_page_id")
    })
@EntityListeners(ApplicationFormPageSectionListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ApplicationFormPageSection extends AbstractRBACModel {

  @Column(name = "name", comment = "Name of the application form section")
  private String name;

  @Column(name = "key", comment = "Unique key for the application form section")
  private String key;

  @Column(name = "description", comment = "Description of the application form section")
  private String description;

  @ManyToOne
  @JoinColumn(
      name = "application_form_page_id",
      foreignKey = @ForeignKey(name = "fk_application_form_page_section_page_id"),
      nullable = false,
      comment = "Reference to the application form page this section belongs to")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private ApplicationFormPage applicationFormPage;

  @OneToMany(mappedBy = "applicationFormPageSection")
  private Set<ApplicationFormSectionCustomField> applicationFormSectionCustomFields;
}

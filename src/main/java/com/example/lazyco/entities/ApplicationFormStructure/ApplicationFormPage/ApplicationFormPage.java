package com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormPage;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractRBACModel;
import com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormPageSection.ApplicationFormPageSection;
import com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormTemplate.ApplicationFormTemplate;
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
    name = "application_form_page",
    comment = "Table storing application form page details",
    indexes = {
      @Index(name = "idx_application_form_page_name", columnList = "name"),
      @Index(name = "idx_application_form_page_key", columnList = "key"),
      @Index(
          name = "idx_application_form_page_template_id",
          columnList = "application_form_template_id")
    })
@EntityListeners(ApplicationFormPageListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ApplicationFormPage extends AbstractRBACModel {

  @Column(name = "name", comment = "Name of the application form page")
  private String name;

  @Column(name = "key", comment = "Unique key for the application form page")
  private String key;

  @Column(name = "description", comment = "Description of the application form page")
  private String description;

  @ManyToOne
  @JoinColumn(
      name = "application_form_template_id",
      foreignKey = @ForeignKey(name = "fk_application_form_page_template_id"),
      nullable = false,
      comment = "Reference to the application form template this page belongs to")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private ApplicationFormTemplate applicationFormTemplate;

  @OneToMany(mappedBy = "applicationFormPage")
  private Set<ApplicationFormPageSection> applicationFormPageSections;
}

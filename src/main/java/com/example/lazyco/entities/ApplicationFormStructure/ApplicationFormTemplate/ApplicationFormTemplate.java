package com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormTemplate;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractRBACModel;
import com.example.lazyco.entities.AdmissionOffer.AdmissionOffer;
import com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormPage.ApplicationFormPage;
import com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormTemplateDocument.ApplicationFormTemplateDocument;
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
    name = "application_form_template",
    comment = "Table storing application form templates.",
    indexes = {
      @Index(name = "idx_application_form_template_code", columnList = "code"),
      @Index(name = "idx_application_form_template_is_active", columnList = "is_active")
    },
    uniqueConstraints =
        @UniqueConstraint(name = "uk_application_form_template_code", columnNames = "code"))
@EntityListeners(ApplicationFormTemplateListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ApplicationFormTemplate extends AbstractRBACModel {

  @Column(
      name = "code",
      nullable = false,
      comment = "Code representing the application form template")
  private String code;

  @Column(name = "name", nullable = false, comment = "Name of the application form template")
  private String name;

  @Column(name = "description", comment = "Description of the application form template")
  private String description;

  @Column(
      name = "is_active",
      columnDefinition = "boolean default true",
      comment = "Indicates whether the application form template is active")
  private Boolean isActive;

  @OneToMany(mappedBy = "applicationFormTemplate")
  private Set<ApplicationFormTemplateDocument> applicationFormTemplateDocuments;

  @OneToMany(mappedBy = "applicationFormTemplate")
  private Set<ApplicationFormPage> applicationFormPages;

  @OneToMany(mappedBy = "applicationFormTemplate")
  private Set<AdmissionOffer> admissionOffers;
}

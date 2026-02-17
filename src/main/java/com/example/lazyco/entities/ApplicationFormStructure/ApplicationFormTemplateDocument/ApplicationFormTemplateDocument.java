package com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormTemplateDocument;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractRBACModel;
import com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormTemplate.ApplicationFormTemplate;
import com.example.lazyco.entities.Document.Document;
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
    name = "application_form_template_document",
    comment = "Table storing documents associated with application form templates",
    indexes = {
      @Index(name = "idx_application_form_document_is_mandatory", columnList = "is_mandatory"),
      @Index(
          name = "idx_application_form_document_template_id",
          columnList = "application_form_template_id"),
      @Index(name = "idx_application_form_document_document_id", columnList = "document_id")
    },
    uniqueConstraints = {
      @UniqueConstraint(
          name = "uk_application_form_document_template_id_document_id",
          columnNames = {"application_form_template_id", "document_id"})
    })
@EntityListeners(ApplicationFormTemplateDocumentListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ApplicationFormTemplateDocument extends AbstractRBACModel {

  @Column(
      name = "is_mandatory",
      columnDefinition = "boolean default false",
      comment = "Whether this document is mandatory for the application form")
  private Boolean isMandatory;

  @ManyToOne
  @JoinColumn(
      name = "application_form_template_id",
      foreignKey = @ForeignKey(name = "fk_application_form_document_template_id"),
      nullable = false,
      comment = "Reference to the application form template this document belongs to")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private ApplicationFormTemplate applicationFormTemplate;

  @ManyToOne
  @JoinColumn(
      name = "document_id",
      foreignKey = @ForeignKey(name = "fk_application_form_document_document_id"),
      nullable = false,
      comment = "Reference to the document this application form document represents")
  private Document document;
}

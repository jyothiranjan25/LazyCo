package com.example.lazyco.entities.ApplicationForm.ApplicationFormDocument;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractRBACModel;
import com.example.lazyco.entities.ApplicationForm.ApplicationForm;
import com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormTemplateDocument.ApplicationFormTemplateDocument;
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
    name = "application_form_document",
    comment = "Table representing documents associated with application forms",
    indexes = {
      @Index(
          name = "idx_application_form_document_application_form_id",
          columnList = "application_form_id"),
      @Index(
          name = "idx_application_form_document_template_document_id",
          columnList = "application_form_template_document_id")
    },
    uniqueConstraints = {
      @UniqueConstraint(
          name = "uk_afd_template_document_id_application_form_id",
          columnNames = {"application_form_template_document_id", "application_form_id"})
    })
@EntityListeners(ApplicationFormDocumentListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ApplicationFormDocument extends AbstractRBACModel {

  @Column(name = "location", comment = "Location of the document file")
  private String location;

  @Column(name = "status", nullable = false, comment = "Status of the document")
  private DocumentStatusEnum status;

  @ManyToOne
  @JoinColumn(
      name = "application_form_id",
      foreignKey = @ForeignKey(name = "fk_application_form_document_application_form_id"),
      nullable = false,
      comment = "Reference to the application form this document belongs to")
  @OnDelete(action = OnDeleteAction.RESTRICT)
  private ApplicationForm applicationForm;

  @ManyToOne
  @JoinColumn(
      name = "application_form_template_document_id",
      foreignKey = @ForeignKey(name = "fk_application_form_document_template_document_id"),
      nullable = false,
      comment = "Reference to the application form template document this document is based on")
  @OnDelete(action = OnDeleteAction.RESTRICT)
  private ApplicationFormTemplateDocument applicationFormTemplateDocument;
}

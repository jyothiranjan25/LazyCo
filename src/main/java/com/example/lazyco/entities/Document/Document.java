package com.example.lazyco.entities.Document;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractModel;
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
    name = "document",
    comment = "Table storing document details",
    indexes = {
      @Index(name = "idx_document_name", columnList = "name"),
      @Index(name = "idx_document_key", columnList = "key"),
      @Index(name = "idx_document_document_type", columnList = "document_type")
    },
    uniqueConstraints = {@UniqueConstraint(name = "uk_document_key", columnNames = "key")})
@EntityListeners(DocumentListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Document extends AbstractModel {

  @Column(name = "name", comment = "Code representing the document")
  private String name;

  @Column(name = "key", comment = "Unique key for the document")
  private String key;

  @Column(name = "document_type", comment = "Type of the document")
  @Enumerated(EnumType.STRING)
  private DocumentTypeEnum documentType;

  @OneToMany(mappedBy = "document")
  private Set<ApplicationFormTemplateDocument> applicationFormTemplateDocuments;
}

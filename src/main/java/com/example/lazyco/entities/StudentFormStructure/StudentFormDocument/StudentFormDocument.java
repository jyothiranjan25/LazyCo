package com.example.lazyco.entities.StudentFormStructure.StudentFormDocument;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractRBACModel;
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
    name = "student_form_document",
    comment = "Table storing documents associated with student forms",
    indexes = {@Index(name = "idx_student_form_document_document_id", columnList = "document_id")})
@EntityListeners(StudentFormDocumentListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class StudentFormDocument extends AbstractRBACModel {

  @Column(
      name = "display_order",
      columnDefinition = "integer default 0",
      comment = "Order of the document within the student form")
  private Integer order;

  @ManyToOne
  @JoinColumn(
      name = "document_id",
      foreignKey = @ForeignKey(name = "fk_student_form_document_document_id"),
      nullable = false,
      comment = "Reference to the document this student form document represents")
  @OnDelete(action = OnDeleteAction.RESTRICT)
  private Document document;
}

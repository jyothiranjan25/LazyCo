package com.example.lazyco.entities.Student.StudentDocument;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractRBACModel;
import com.example.lazyco.entities.Student.Student;
import com.example.lazyco.entities.StudentFormStructure.StudentFormDocument.StudentFormDocument;
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
    name = "student_document",
    comment = "Table representing documents associated with student",
    indexes = {
      @Index(name = "idx_student_document_student_id", columnList = "student_id"),
      @Index(
          name = "idx_student_document_student_form_document_id",
          columnList = "student_form_document_id")
    },
    uniqueConstraints = {
      @UniqueConstraint(
          name = "uk_afd_template_document_id_student_form_id",
          columnNames = {"student_form_document_id", "student_id"})
    })
@EntityListeners(StudentDocumentListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class StudentDocument extends AbstractRBACModel {

  @Column(name = "location", comment = "Location of the document file")
  private String location;

  @Column(name = "status", nullable = false, comment = "Status of the document")
  private DocumentStatusEnum status;

  @ManyToOne
  @JoinColumn(
      name = "student_id",
      foreignKey = @ForeignKey(name = "fk_student_document_student_id"),
      nullable = false,
      comment = "Reference to the student this document belongs to")
  @OnDelete(action = OnDeleteAction.RESTRICT)
  private Student student;

  @ManyToOne
  @JoinColumn(
      name = "student_form_document_id",
      foreignKey = @ForeignKey(name = "fk_student_form_document_template_document_id"),
      nullable = false,
      comment = "Reference to the student form template document this document is based on")
  @OnDelete(action = OnDeleteAction.RESTRICT)
  private StudentFormDocument studentFormDocument;
}

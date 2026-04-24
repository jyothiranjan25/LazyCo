package com.example.lazyco.entities.Student.StudentDocument;

import jakarta.persistence.*;

public class StudentDocumentListener {

  @PrePersist
  public void prePersist(StudentDocument studentDocument) {
    if (studentDocument.getStatus() == null) {
      studentDocument.setStatus(DocumentStatusEnum.PENDING);
    }
  }

  @PreUpdate
  public void preUpdate(StudentDocument studentDocument) {
    // Logic to execute before persisting entity
  }

  @PreRemove
  public void preRemove(StudentDocument studentDocument) {
    // Logic to execute before persisting entity
  }

  @PostPersist
  @PostUpdate
  @PostRemove
  public void deleteCache(StudentDocument studentDocument) {}
}

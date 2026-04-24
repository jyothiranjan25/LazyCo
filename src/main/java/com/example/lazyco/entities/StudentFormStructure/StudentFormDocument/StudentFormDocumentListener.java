package com.example.lazyco.entities.StudentFormStructure.StudentFormDocument;

import jakarta.persistence.*;

public class StudentFormDocumentListener {

  @PrePersist
  public void prePersist(StudentFormDocument studentFormDocument) {
    // Logic to execute before persisting entity
  }

  @PreUpdate
  public void preUpdate(StudentFormDocument studentFormDocument) {
    // Logic to execute before persisting entity
  }

  @PreRemove
  public void preRemove(StudentFormDocument studentFormDocument) {
    // Logic to execute before persisting entity
  }

  @PostPersist
  @PostUpdate
  @PostRemove
  public void deleteCache(StudentFormDocument studentFormDocument) {}
}

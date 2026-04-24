package com.example.lazyco.entities.StudentFormStructure.StudentFormPage;

import jakarta.persistence.*;

public class StudentFormPageListener {

  @PrePersist
  public void prePersist(StudentFormPage studentFormPage) {
    // Logic to execute before persisting entity
  }

  @PreUpdate
  public void preUpdate(StudentFormPage studentFormPage) {
    // Logic to execute before persisting entity
  }

  @PreRemove
  public void preRemove(StudentFormPage studentFormPage) {
    // Logic to execute before persisting entity
  }

  @PostPersist
  @PostUpdate
  @PostRemove
  public void deleteCache(StudentFormPage studentFormPage) {}
}

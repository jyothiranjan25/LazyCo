package com.example.lazyco.entities.Student.StudentCustomField;

import jakarta.persistence.*;

public class StudentCustomFieldListener {

  @PrePersist
  public void prePersist(StudentCustomField studentCustomField) {
    // Logic to execute before persisting entity
  }

  @PreUpdate
  public void preUpdate(StudentCustomField studentCustomField) {
    // Logic to execute before persisting entity
  }

  @PreRemove
  public void preRemove(StudentCustomField studentCustomField) {
    // Logic to execute before persisting entity
  }

  @PostPersist
  @PostUpdate
  @PostRemove
  public void deleteCache(StudentCustomField studentCustomField) {}
}

package com.example.lazyco.entities.StudentFormStructure.StudentFormPageSection;

import jakarta.persistence.*;

public class StudentFormPageSectionListener {

  @PrePersist
  public void prePersist(StudentFormPageSection studentFormPageSection) {
    // Logic to execute before persisting entity
  }

  @PreUpdate
  public void preUpdate(StudentFormPageSection studentFormPageSection) {
    // Logic to execute before persisting entity
  }

  @PreRemove
  public void preRemove(StudentFormPageSection studentFormPageSection) {
    // Logic to execute before persisting entity
  }

  @PostPersist
  @PostUpdate
  @PostRemove
  public void deleteCache(StudentFormPageSection studentFormPageSection) {}
}

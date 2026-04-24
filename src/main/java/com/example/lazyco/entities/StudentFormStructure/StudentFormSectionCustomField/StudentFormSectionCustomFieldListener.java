package com.example.lazyco.entities.StudentFormStructure.StudentFormSectionCustomField;

import jakarta.persistence.*;

public class StudentFormSectionCustomFieldListener {

  @PrePersist
  public void prePersist(StudentFormSectionCustomField studentFormSectionCustomField) {
    // Logic to execute before persisting entity
  }

  @PreUpdate
  public void preUpdate(StudentFormSectionCustomField studentFormSectionCustomField) {
    // Logic to execute before persisting entity
  }

  @PreRemove
  public void preRemove(StudentFormSectionCustomField studentFormSectionCustomField) {
    // Logic to execute before persisting entity
  }

  @PostPersist
  @PostUpdate
  @PostRemove
  public void deleteCache(StudentFormSectionCustomField studentFormSectionCustomField) {}
}

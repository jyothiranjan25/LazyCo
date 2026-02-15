package com.example.lazyco.entities.CustomField;

import jakarta.persistence.*;

public class CustomFieldListener {

  @PrePersist
  public void prePersist(CustomField customField) {
    // Logic to execute before persisting entity
  }

  @PreUpdate
  public void preUpdate(CustomField customField) {
    // Logic to execute before persisting entity
  }

  @PreRemove
  public void preRemove(CustomField customField) {
    // Logic to execute before persisting entity
  }

  @PostPersist
  @PostUpdate
  @PostRemove
  public void deleteCache(CustomField customField) {}
}

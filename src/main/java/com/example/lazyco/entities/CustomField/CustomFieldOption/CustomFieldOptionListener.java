package com.example.lazyco.entities.CustomField.CustomFieldOption;

import jakarta.persistence.*;

public class CustomFieldOptionListener {

  @PrePersist
  public void prePersist(CustomFieldOption customFieldOption) {
    // Logic to execute before persisting entity
  }

  @PreUpdate
  public void preUpdate(CustomFieldOption customFieldOption) {
    // Logic to execute before persisting entity
  }

  @PreRemove
  public void preRemove(CustomFieldOption customFieldOption) {
    // Logic to execute before persisting entity
  }

  @PostPersist
  @PostUpdate
  @PostRemove
  public void deleteCache(CustomFieldOption customFieldOption) {}
}

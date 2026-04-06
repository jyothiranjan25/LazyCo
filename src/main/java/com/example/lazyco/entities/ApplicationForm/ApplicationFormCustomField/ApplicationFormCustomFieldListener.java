package com.example.lazyco.entities.ApplicationForm.ApplicationFormCustomField;

import jakarta.persistence.*;

public class ApplicationFormCustomFieldListener {

  @PrePersist
  public void prePersist(ApplicationFormCustomField applicationFormCustomField) {
    // Logic to execute before persisting entity
  }

  @PreUpdate
  public void preUpdate(ApplicationFormCustomField applicationFormCustomField) {
    // Logic to execute before persisting entity
  }

  @PreRemove
  public void preRemove(ApplicationFormCustomField applicationFormCustomField) {
    // Logic to execute before persisting entity
  }

  @PostPersist
  @PostUpdate
  @PostRemove
  public void deleteCache(ApplicationFormCustomField applicationFormCustomField) {}
}

package com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormTemplate;

import jakarta.persistence.*;

public class ApplicationFormTemplateListener {

  @PrePersist
  public void prePersist(ApplicationFormTemplate applicationFormTemplate) {
    // Logic to execute before persisting entity
  }

  @PreUpdate
  public void preUpdate(ApplicationFormTemplate applicationFormTemplate) {
    // Logic to execute before persisting entity
  }

  @PreRemove
  public void preRemove(ApplicationFormTemplate applicationFormTemplate) {
    // Logic to execute before persisting entity
  }

  @PostPersist
  @PostUpdate
  @PostRemove
  public void deleteCache(ApplicationFormTemplate applicationFormTemplate) {}
}

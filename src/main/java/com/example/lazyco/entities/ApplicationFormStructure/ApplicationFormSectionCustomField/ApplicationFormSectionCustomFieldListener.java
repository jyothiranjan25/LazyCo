package com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormSectionCustomField;

import jakarta.persistence.*;

public class ApplicationFormSectionCustomFieldListener {

  @PrePersist
  public void prePersist(ApplicationFormSectionCustomField applicationFormSectionCustomField) {
    // Logic to execute before persisting entity
  }

  @PreUpdate
  public void preUpdate(ApplicationFormSectionCustomField applicationFormSectionCustomField) {
    // Logic to execute before persisting entity
  }

  @PreRemove
  public void preRemove(ApplicationFormSectionCustomField applicationFormSectionCustomField) {
    // Logic to execute before persisting entity
  }

  @PostPersist
  @PostUpdate
  @PostRemove
  public void deleteCache(ApplicationFormSectionCustomField applicationFormSectionCustomField) {}
}

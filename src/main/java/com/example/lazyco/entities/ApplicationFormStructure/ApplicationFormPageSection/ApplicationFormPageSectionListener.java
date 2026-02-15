package com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormPageSection;

import jakarta.persistence.*;

public class ApplicationFormPageSectionListener {

  @PrePersist
  public void prePersist(ApplicationFormPageSection applicationFormPageSection) {
    // Logic to execute before persisting entity
  }

  @PreUpdate
  public void preUpdate(ApplicationFormPageSection applicationFormPageSection) {
    // Logic to execute before persisting entity
  }

  @PreRemove
  public void preRemove(ApplicationFormPageSection applicationFormPageSection) {
    // Logic to execute before persisting entity
  }

  @PostPersist
  @PostUpdate
  @PostRemove
  public void deleteCache(ApplicationFormPageSection applicationFormPageSection) {}
}

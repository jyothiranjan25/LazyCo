package com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormPage;

import jakarta.persistence.*;

public class ApplicationFormPageListener {

  @PrePersist
  public void prePersist(ApplicationFormPage applicationFormPage) {
    // Logic to execute before persisting entity
  }

  @PreUpdate
  public void preUpdate(ApplicationFormPage applicationFormPage) {
    // Logic to execute before persisting entity
  }

  @PreRemove
  public void preRemove(ApplicationFormPage applicationFormPage) {
    // Logic to execute before persisting entity
  }

  @PostPersist
  @PostUpdate
  @PostRemove
  public void deleteCache(ApplicationFormPage applicationFormPage) {}
}

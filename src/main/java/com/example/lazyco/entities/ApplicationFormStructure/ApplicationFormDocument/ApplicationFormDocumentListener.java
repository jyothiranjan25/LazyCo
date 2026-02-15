package com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormDocument;

import jakarta.persistence.*;

public class ApplicationFormDocumentListener {

  @PrePersist
  public void prePersist(ApplicationFormDocument applicationFormDocument) {
    // Logic to execute before persisting entity
  }

  @PreUpdate
  public void preUpdate(ApplicationFormDocument applicationFormDocument) {
    // Logic to execute before persisting entity
  }

  @PreRemove
  public void preRemove(ApplicationFormDocument applicationFormDocument) {
    // Logic to execute before persisting entity
  }

  @PostPersist
  @PostUpdate
  @PostRemove
  public void deleteCache(ApplicationFormDocument applicationFormDocument) {}
}

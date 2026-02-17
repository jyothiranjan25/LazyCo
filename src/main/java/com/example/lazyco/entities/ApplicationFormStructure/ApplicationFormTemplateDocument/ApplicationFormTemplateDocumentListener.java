package com.example.lazyco.entities.ApplicationFormStructure.ApplicationFormTemplateDocument;

import jakarta.persistence.*;

public class ApplicationFormTemplateDocumentListener {

  @PrePersist
  public void prePersist(ApplicationFormTemplateDocument applicationFormTemplateDocument) {
    // Logic to execute before persisting entity
  }

  @PreUpdate
  public void preUpdate(ApplicationFormTemplateDocument applicationFormTemplateDocument) {
    // Logic to execute before persisting entity
  }

  @PreRemove
  public void preRemove(ApplicationFormTemplateDocument applicationFormTemplateDocument) {
    // Logic to execute before persisting entity
  }

  @PostPersist
  @PostUpdate
  @PostRemove
  public void deleteCache(ApplicationFormTemplateDocument applicationFormTemplateDocument) {}
}

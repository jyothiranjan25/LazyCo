package com.example.lazyco.entities.ApplicationForm.ApplicationFormDocument;

import jakarta.persistence.*;

public class ApplicationFormDocumentListener {

  @PrePersist
  public void prePersist(ApplicationFormDocument applicationFormDocument) {
    if (applicationFormDocument.getStatus() == null) {
      applicationFormDocument.setStatus(DocumentStatusEnum.PENDING);
    }
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

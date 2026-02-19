package com.example.lazyco.entities.ApplicationForm;

import jakarta.persistence.*;

public class ApplicationFormListener {

  @PrePersist
  public void prePersist(ApplicationForm applicationForm) {
    // Logic to execute before persisting entity
  }

  @PreUpdate
  public void preUpdate(ApplicationForm applicationForm) {
    // Logic to execute before persisting entity
  }

  @PreRemove
  public void preRemove(ApplicationForm applicationForm) {
    // Logic to execute before persisting entity
  }

  @PostPersist
  @PostUpdate
  @PostRemove
  public void deleteCache(ApplicationForm applicationForm) {}
}

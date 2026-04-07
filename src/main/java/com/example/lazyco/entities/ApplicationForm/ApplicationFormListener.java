package com.example.lazyco.entities.ApplicationForm;

import jakarta.persistence.*;
import java.time.LocalDateTime;

public class ApplicationFormListener {

  @PrePersist
  public void prePersist(ApplicationForm applicationForm) {
    // Logic to execute before persisting entity
    if (applicationForm.getApplicationDate() == null) {
      applicationForm.setApplicationDate(LocalDateTime.now());
    }
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

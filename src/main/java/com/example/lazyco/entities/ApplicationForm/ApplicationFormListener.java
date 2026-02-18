package com.example.lazyco.entities.ApplicationForm;

import jakarta.persistence.*;
import java.time.LocalDate;

public class ApplicationFormListener {

  @PrePersist
  public void prePersist(ApplicationForm applicationForm) {
    // Logic to execute before persisting entity
    if (applicationForm.getEnrollmentDate() != null) {
      applicationForm.setEnrollmentDate(LocalDate.now());
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

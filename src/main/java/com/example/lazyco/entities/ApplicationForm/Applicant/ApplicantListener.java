package com.example.lazyco.entities.ApplicationForm.Applicant;

import jakarta.persistence.*;

public class ApplicantListener {

  @PrePersist
  public void prePersist(Applicant applicant) {
    // Logic to execute before persisting entity
  }

  @PreUpdate
  public void preUpdate(Applicant applicant) {
    // Logic to execute before persisting entity
  }

  @PreRemove
  public void preRemove(Applicant applicant) {
    // Logic to execute before persisting entity
  }

  @PostPersist
  @PostUpdate
  @PostRemove
  public void deleteCache(Applicant applicant) {}
}

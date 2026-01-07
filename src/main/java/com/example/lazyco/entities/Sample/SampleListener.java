package com.example.lazyco.backend.entities.Sample;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;

public class SampleListener {

  @PrePersist
  public void prePersist(Sample sample) {
    // Logic to execute before persisting an AppUser entity
  }

  @PreUpdate
  public void preUpdate(Sample sample) {
    // Logic to execute before persisting an AppUser entity
  }

  @PreRemove
  public void preRemove(Sample sample) {
    // Logic to execute before persisting an AppUser entity
  }
}

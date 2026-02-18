package com.example.lazyco.entities.Admission;

import jakarta.persistence.*;

public class AdmissionListener {

  @PrePersist
  public void prePersist(Admission admission) {
    // Logic to execute before persisting entity
  }

  @PreUpdate
  public void preUpdate(Admission admission) {
    // Logic to execute before persisting entity
  }

  @PreRemove
  public void preRemove(Admission admission) {
    // Logic to execute before persisting entity
  }

  @PostPersist
  @PostUpdate
  @PostRemove
  public void deleteCache(Admission admission) {}
}

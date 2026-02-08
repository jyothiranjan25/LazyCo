package com.example.lazyco.entities.University;

import jakarta.persistence.*;

public class UniversityListener {

  @PrePersist
  public void prePersist(University university) {
    // Logic to execute before persisting entity
  }

  @PreUpdate
  public void preUpdate(University university) {
    // Logic to execute before persisting entity
  }

  @PreRemove
  public void preRemove(University university) {
    // Logic to execute before persisting entity
  }

  @PostPersist
  @PostUpdate
  @PostRemove
  public void deleteCache(University university) {}
}

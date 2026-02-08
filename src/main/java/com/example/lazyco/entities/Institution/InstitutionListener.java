package com.example.lazyco.entities.Institution;

import jakarta.persistence.*;

public class InstitutionListener {

  @PrePersist
  public void prePersist(Institution institution) {
    // Logic to execute before persisting entity
  }

  @PreUpdate
  public void preUpdate(Institution institution) {
    // Logic to execute before persisting entity
  }

  @PreRemove
  public void preRemove(Institution institution) {
    // Logic to execute before persisting entity
  }

  @PostPersist
  @PostUpdate
  @PostRemove
  public void deleteCache(Institution institution) {}
}

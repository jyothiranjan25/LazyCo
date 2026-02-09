package com.example.lazyco.entities.TermSystem;

import jakarta.persistence.*;

public class TermSystemListener {

  @PrePersist
  public void prePersist(TermSystem termSystem) {
    // Logic to execute before persisting entity
  }

  @PreUpdate
  public void preUpdate(TermSystem termSystem) {
    // Logic to execute before persisting entity
  }

  @PreRemove
  public void preRemove(TermSystem termSystem) {
    // Logic to execute before persisting entity
  }

  @PostPersist
  @PostUpdate
  @PostRemove
  public void deleteCache(TermSystem termSystem) {}
}

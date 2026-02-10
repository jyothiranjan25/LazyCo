package com.example.lazyco.entities.TermCycle;

import jakarta.persistence.*;

public class TermCycleListener {

  @PrePersist
  public void prePersist(TermCycle termCycle) {
    // Logic to execute before persisting entity
  }

  @PreUpdate
  public void preUpdate(TermCycle termCycle) {
    // Logic to execute before persisting entity
  }

  @PreRemove
  public void preRemove(TermCycle termCycle) {
    // Logic to execute before persisting entity
  }

  @PostPersist
  @PostUpdate
  @PostRemove
  public void deleteCache(TermCycle termCycle) {}
}

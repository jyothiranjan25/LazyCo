package com.example.lazyco.entities.ProgramCycle;

import jakarta.persistence.*;

public class ProgramCycleListener {

  @PrePersist
  public void prePersist(ProgramCycle programCycle) {
    // Logic to execute before persisting entity
  }

  @PreUpdate
  public void preUpdate(ProgramCycle programCycle) {
    // Logic to execute before persisting entity
  }

  @PreRemove
  public void preRemove(ProgramCycle programCycle) {
    // Logic to execute before persisting entity
  }

  @PostPersist
  @PostUpdate
  @PostRemove
  public void deleteCache(ProgramCycle programCycle) {}
}

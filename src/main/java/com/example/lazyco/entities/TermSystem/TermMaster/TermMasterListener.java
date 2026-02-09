package com.example.lazyco.entities.TermSystem.TermMaster;

import jakarta.persistence.*;

public class TermMasterListener {

  @PrePersist
  public void prePersist(TermMaster termMaster) {
    // Logic to execute before persisting entity
  }

  @PreUpdate
  public void preUpdate(TermMaster termMaster) {
    // Logic to execute before persisting entity
  }

  @PreRemove
  public void preRemove(TermMaster termMaster) {
    // Logic to execute before persisting entity
  }

  @PostPersist
  @PostUpdate
  @PostRemove
  public void deleteCache(TermMaster termMaster) {}
}

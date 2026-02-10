package com.example.lazyco.entities.AcademicProgram.ProgramTermSystem;

import jakarta.persistence.*;

public class ProgramTermSystemListener {

  @PrePersist
  public void prePersist(ProgramTermSystem programTermSystem) {
    // Logic to execute before persisting entity
  }

  @PreUpdate
  public void preUpdate(ProgramTermSystem programTermSystem) {
    // Logic to execute before persisting entity
  }

  @PreRemove
  public void preRemove(ProgramTermSystem programTermSystem) {
    // Logic to execute before persisting entity
  }

  @PostPersist
  @PostUpdate
  @PostRemove
  public void deleteCache(ProgramTermSystem programTermSystem) {}
}

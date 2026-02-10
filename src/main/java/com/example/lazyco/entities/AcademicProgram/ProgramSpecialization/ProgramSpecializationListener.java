package com.example.lazyco.entities.AcademicProgram.ProgramSpecialization;

import jakarta.persistence.*;

public class ProgramSpecializationListener {

  @PrePersist
  public void prePersist(ProgramSpecialization programSpecialization) {
    // Logic to execute before persisting entity
  }

  @PreUpdate
  public void preUpdate(ProgramSpecialization programSpecialization) {
    // Logic to execute before persisting entity
  }

  @PreRemove
  public void preRemove(ProgramSpecialization programSpecialization) {
    // Logic to execute before persisting entity
  }

  @PostPersist
  @PostUpdate
  @PostRemove
  public void deleteCache(ProgramSpecialization programSpecialization) {}
}

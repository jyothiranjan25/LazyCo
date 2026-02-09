package com.example.lazyco.entities.AcademicProgram;

import jakarta.persistence.*;

public class AcademicProgramListener {

  @PrePersist
  public void prePersist(AcademicProgram academicProgram) {
    // Logic to execute before persisting entity
  }

  @PreUpdate
  public void preUpdate(AcademicProgram academicProgram) {
    // Logic to execute before persisting entity
  }

  @PreRemove
  public void preRemove(AcademicProgram academicProgram) {
    // Logic to execute before persisting entity
  }

  @PostPersist
  @PostUpdate
  @PostRemove
  public void deleteCache(AcademicProgram academicProgram) {}
}

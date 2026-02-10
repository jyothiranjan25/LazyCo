package com.example.lazyco.entities.AcademicProgram.ProgramTermMaster;

import jakarta.persistence.*;

public class ProgramTermMasterListener {

  @PrePersist
  public void prePersist(ProgramTermMaster programTermMaster) {
    // Logic to execute before persisting entity
  }

  @PreUpdate
  public void preUpdate(ProgramTermMaster programTermMaster) {
    // Logic to execute before persisting entity
  }

  @PreRemove
  public void preRemove(ProgramTermMaster programTermMaster) {
    // Logic to execute before persisting entity
  }

  @PostPersist
  @PostUpdate
  @PostRemove
  public void deleteCache(ProgramTermMaster programTermMaster) {}
}

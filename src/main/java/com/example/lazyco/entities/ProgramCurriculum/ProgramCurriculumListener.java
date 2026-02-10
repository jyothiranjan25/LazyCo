package com.example.lazyco.entities.ProgramCurriculum;

import jakarta.persistence.*;

public class ProgramCurriculumListener {

  @PrePersist
  public void prePersist(ProgramCurriculum programCurriculum) {
    // Logic to execute before persisting entity
  }

  @PreUpdate
  public void preUpdate(ProgramCurriculum programCurriculum) {
    // Logic to execute before persisting entity
  }

  @PreRemove
  public void preRemove(ProgramCurriculum programCurriculum) {
    // Logic to execute before persisting entity
  }

  @PostPersist
  @PostUpdate
  @PostRemove
  public void deleteCache(ProgramCurriculum programCurriculum) {}
}

package com.example.lazyco.entities.SyllabusMaster.SyllabusCourse;

import jakarta.persistence.*;

public class SyllabusCourseListener {

  @PrePersist
  public void prePersist(SyllabusCourse syllabusCourse) {
    // Logic to execute before persisting entity
  }

  @PreUpdate
  public void preUpdate(SyllabusCourse syllabusCourse) {
    // Logic to execute before persisting entity
  }

  @PreRemove
  public void preRemove(SyllabusCourse syllabusCourse) {
    // Logic to execute before persisting entity
  }

  @PostPersist
  @PostUpdate
  @PostRemove
  public void deleteCache(SyllabusCourse syllabusCourse) {}
}

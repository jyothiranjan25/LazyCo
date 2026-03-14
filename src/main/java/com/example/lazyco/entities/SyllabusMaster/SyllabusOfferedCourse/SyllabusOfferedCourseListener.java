package com.example.lazyco.entities.SyllabusMaster.SyllabusOfferedCourse;

import jakarta.persistence.*;

public class SyllabusOfferedCourseListener {

  @PrePersist
  public void prePersist(SyllabusOfferedCourse syllabusOfferedCourse) {
    // Logic to execute before persisting entity
  }

  @PreUpdate
  public void preUpdate(SyllabusOfferedCourse syllabusOfferedCourse) {
    // Logic to execute before persisting entity
  }

  @PreRemove
  public void preRemove(SyllabusOfferedCourse syllabusOfferedCourse) {
    // Logic to execute before persisting entity
  }

  @PostPersist
  @PostUpdate
  @PostRemove
  public void deleteCache(SyllabusOfferedCourse syllabusOfferedCourse) {}
}

package com.example.lazyco.entities.CourseMaster;

import jakarta.persistence.*;

public class CourseListener {

  @PrePersist
  public void prePersist(Course course) {
    // Logic to execute before persisting entity
  }

  @PreUpdate
  public void preUpdate(Course course) {
    // Logic to execute before persisting entity
  }

  @PreRemove
  public void preRemove(Course course) {
    // Logic to execute before persisting entity
  }

  @PostPersist
  @PostUpdate
  @PostRemove
  public void deleteCache(Course course) {}
}

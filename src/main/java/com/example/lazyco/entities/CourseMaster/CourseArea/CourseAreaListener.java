package com.example.lazyco.entities.CourseMaster.CourseArea;

import jakarta.persistence.*;

public class CourseAreaListener {

  @PrePersist
  public void prePersist(CourseArea courseArea) {
    // Logic to execute before persisting entity
  }

  @PreUpdate
  public void preUpdate(CourseArea courseArea) {
    // Logic to execute before persisting entity
  }

  @PreRemove
  public void preRemove(CourseArea courseArea) {
    // Logic to execute before persisting entity
  }

  @PostPersist
  @PostUpdate
  @PostRemove
  public void deleteCache(CourseArea courseArea) {}
}

package com.example.lazyco.entities.SyllabusMaster.CourseCategory;

import jakarta.persistence.*;

public class CourseCategoryListener {

  @PrePersist
  public void prePersist(CourseCategory courseCategory) {
    // Logic to execute before persisting entity
  }

  @PreUpdate
  public void preUpdate(CourseCategory courseCategory) {
    // Logic to execute before persisting entity
  }

  @PreRemove
  public void preRemove(CourseCategory courseCategory) {
    // Logic to execute before persisting entity
  }

  @PostPersist
  @PostUpdate
  @PostRemove
  public void deleteCache(CourseCategory courseCategory) {}
}

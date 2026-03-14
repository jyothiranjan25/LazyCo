package com.example.lazyco.entities.CourseMaster.CourseClassType;

import jakarta.persistence.*;

public class CourseClassTypeListener {

  @PrePersist
  public void prePersist(CourseClassType courseClassType) {
    // Logic to execute before persisting entity
  }

  @PreUpdate
  public void preUpdate(CourseClassType courseClassType) {
    // Logic to execute before persisting entity
  }

  @PreRemove
  public void preRemove(CourseClassType courseClassType) {
    // Logic to execute before persisting entity
  }

  @PostPersist
  @PostUpdate
  @PostRemove
  public void deleteCache(CourseClassType courseClassType) {}
}

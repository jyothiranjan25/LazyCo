package com.example.lazyco.entities.CourseMaster.CourseCredit;

import jakarta.persistence.*;

public class CourseCreditListener {

  @PrePersist
  public void prePersist(CourseCredit courseCredit) {
    // Logic to execute before persisting entity
  }

  @PreUpdate
  public void preUpdate(CourseCredit courseCredit) {
    // Logic to execute before persisting entity
  }

  @PreRemove
  public void preRemove(CourseCredit courseCredit) {
    // Logic to execute before persisting entity
  }

  @PostPersist
  @PostUpdate
  @PostRemove
  public void deleteCache(CourseCredit courseCredit) {}
}

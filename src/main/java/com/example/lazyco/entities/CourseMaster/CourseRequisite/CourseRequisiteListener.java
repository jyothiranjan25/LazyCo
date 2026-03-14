package com.example.lazyco.entities.CourseMaster.CourseRequisite;

import jakarta.persistence.*;

public class CourseRequisiteListener {

  @PrePersist
  public void prePersist(CourseRequisite courseRequisite) {
    // Logic to execute before persisting entity
  }

  @PreUpdate
  public void preUpdate(CourseRequisite courseRequisite) {
    // Logic to execute before persisting entity
  }

  @PreRemove
  public void preRemove(CourseRequisite courseRequisite) {
    // Logic to execute before persisting entity
  }

  @PostPersist
  @PostUpdate
  @PostRemove
  public void deleteCache(CourseRequisite courseRequisite) {}
}

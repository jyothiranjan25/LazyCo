package com.example.lazyco.entities.CourseMaster.ClassType;

import jakarta.persistence.*;

public class ClassTypeListener {

  @PrePersist
  public void prePersist(ClassType classType) {
    // Logic to execute before persisting entity
  }

  @PreUpdate
  public void preUpdate(ClassType classType) {
    // Logic to execute before persisting entity
  }

  @PreRemove
  public void preRemove(ClassType classType) {
    // Logic to execute before persisting entity
  }

  @PostPersist
  @PostUpdate
  @PostRemove
  public void deleteCache(ClassType classType) {}
}

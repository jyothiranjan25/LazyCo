package com.example.lazyco.entities.Student;

import jakarta.persistence.*;

public class StudentListener {

  @PrePersist
  public void prePersist(Student student) {
    // Logic to execute before persisting entity
  }

  @PreUpdate
  public void preUpdate(Student student) {
    // Logic to execute before persisting entity
  }

  @PreRemove
  public void preRemove(Student student) {
    // Logic to execute before persisting entity
  }

  @PostPersist
  @PostUpdate
  @PostRemove
  public void deleteCache(Student student) {}
}

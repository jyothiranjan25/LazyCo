package com.example.lazyco.entities.AcademicYear;

import jakarta.persistence.*;

public class AcademicYearListener {

  @PrePersist
  public void prePersist(AcademicYear academicYear) {
    // Logic to execute before persisting entity
  }

  @PreUpdate
  public void preUpdate(AcademicYear academicYear) {
    // Logic to execute before persisting entity
  }

  @PreRemove
  public void preRemove(AcademicYear academicYear) {
    // Logic to execute before persisting entity
  }

  @PostPersist
  @PostUpdate
  @PostRemove
  public void deleteCache(AcademicYear academicYear) {}
}

package com.example.lazyco.entities.Admission;

import jakarta.persistence.*;
import java.time.LocalDateTime;

public class AdmissionListener {

  @PrePersist
  public void prePersist(Admission admission) {
    // Logic to execute before persisting entity
    if (admission.getAdmissionDate() == null) {
      admission.setAdmissionDate(LocalDateTime.now());
    }
    if (admission.getAdmissionStatus() == null) {
      admission.setAdmissionStatus(AdmissionStatusEnum.ACTIVE);
    }
  }

  @PreUpdate
  public void preUpdate(Admission admission) {
    // Logic to execute before persisting entity
  }

  @PreRemove
  public void preRemove(Admission admission) {
    // Logic to execute before persisting entity
  }

  @PostPersist
  @PostUpdate
  @PostRemove
  public void deleteCache(Admission admission) {}
}

package com.example.lazyco.entities.AdmissionOffer.AdmissionOfferProgram;

import jakarta.persistence.*;

public class AdmissionOfferProgramListener {

  @PrePersist
  public void prePersist(AdmissionOfferProgram admissionOfferProgram) {
    // Logic to execute before persisting entity
  }

  @PreUpdate
  public void preUpdate(AdmissionOfferProgram admissionOfferProgram) {
    // Logic to execute before persisting entity
  }

  @PreRemove
  public void preRemove(AdmissionOfferProgram admissionOfferProgram) {
    // Logic to execute before persisting entity
  }

  @PostPersist
  @PostUpdate
  @PostRemove
  public void deleteCache(AdmissionOfferProgram admissionOfferProgram) {}
}

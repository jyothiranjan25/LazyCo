package com.example.lazyco.entities.AdmissionOffer;

import jakarta.persistence.*;

public class AdmissionOfferListener {

  @PrePersist
  public void prePersist(AdmissionOffer admissionOffer) {
    // Logic to execute before persisting entity
  }

  @PreUpdate
  public void preUpdate(AdmissionOffer admissionOffer) {
    // Logic to execute before persisting entity
  }

  @PreRemove
  public void preRemove(AdmissionOffer admissionOffer) {
    // Logic to execute before persisting entity
  }

  @PostPersist
  @PostUpdate
  @PostRemove
  public void deleteCache(AdmissionOffer admissionOffer) {}
}

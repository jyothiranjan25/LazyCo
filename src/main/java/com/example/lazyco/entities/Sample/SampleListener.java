package com.example.lazyco.entities.Sample;

import jakarta.persistence.*;

public class SampleListener {

  @PrePersist
  public void prePersist(Sample sample) {
    // Logic to execute before persisting entity
  }

  @PreUpdate
  public void preUpdate(Sample sample) {
    // Logic to execute before persisting entity
  }

  @PreRemove
  public void preRemove(Sample sample) {
    // Logic to execute before persisting entity
  }

  @PostPersist
  @PostUpdate
  @PostRemove
  public void deleteCache(Sample sample) {}
}

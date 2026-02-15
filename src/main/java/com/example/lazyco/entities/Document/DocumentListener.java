package com.example.lazyco.entities.Document;

import jakarta.persistence.*;

public class DocumentListener {

  @PrePersist
  public void prePersist(Document document) {
    // Logic to execute before persisting entity
  }

  @PreUpdate
  public void preUpdate(Document document) {
    // Logic to execute before persisting entity
  }

  @PreRemove
  public void preRemove(Document document) {
    // Logic to execute before persisting entity
  }

  @PostPersist
  @PostUpdate
  @PostRemove
  public void deleteCache(Document document) {}
}

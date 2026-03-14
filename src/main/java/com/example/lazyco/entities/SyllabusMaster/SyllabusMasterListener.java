package com.example.lazyco.entities.SyllabusMaster;

import jakarta.persistence.*;

public class SyllabusMasterListener {

  @PrePersist
  public void prePersist(SyllabusMaster syllabusMaster) {
    // Logic to execute before persisting entity
  }

  @PreUpdate
  public void preUpdate(SyllabusMaster syllabusMaster) {
    // Logic to execute before persisting entity
  }

  @PreRemove
  public void preRemove(SyllabusMaster syllabusMaster) {
    // Logic to execute before persisting entity
  }

  @PostPersist
  @PostUpdate
  @PostRemove
  public void deleteCache(SyllabusMaster syllabusMaster) {}
}

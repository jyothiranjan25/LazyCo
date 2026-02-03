package com.example.lazyco.entities.UserManagement.Role;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;

public class RoleListener {

  @PrePersist
  public void prePersist(Role role) {
    // Logic to execute before persisting entity
  }

  @PreUpdate
  public void preUpdate(Role role) {
    // Logic to execute before persisting entity
  }

  @PreRemove
  public void preRemove(Role role) {
    // Logic to execute before persisting entity
  }
}

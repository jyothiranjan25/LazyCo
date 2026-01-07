package com.example.lazyco.backend.entities.UserManagement.Role;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;

public class RoleListener {

  @PrePersist
  public void prePersist(Role role) {
    // Logic to execute before persisting an AppUser entity
  }

  @PreUpdate
  public void preUpdate(Role role) {
    // Logic to execute before persisting an AppUser entity
  }

  @PreRemove
  public void preRemove(Role role) {
    // Logic to execute before persisting an AppUser entity
  }
}

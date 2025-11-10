package com.example.lazyco.backend.entities.UserManagement.UserRole;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;

public class UserRoleListener {

  @PrePersist
  public void prePersist(UserRole userRole) {
    // Logic to execute before persisting an AppUser entity
  }

  @PreUpdate
  public void preUpdate(UserRole userRole) {
    // Logic to execute before persisting an AppUser entity
  }

  @PreRemove
  public void preRemove(UserRole userRole) {
    // Logic to execute before persisting an AppUser entity
  }
}

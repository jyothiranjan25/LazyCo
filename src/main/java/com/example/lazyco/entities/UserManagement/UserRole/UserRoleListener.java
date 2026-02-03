package com.example.lazyco.entities.UserManagement.UserRole;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;

public class UserRoleListener {

  @PrePersist
  public void prePersist(UserRole userRole) {
    // Logic to execute before persisting entity
  }

  @PreUpdate
  public void preUpdate(UserRole userRole) {
    // Logic to execute before persisting entity
  }

  @PreRemove
  public void preRemove(UserRole userRole) {
    // Logic to execute before persisting entity
  }
}

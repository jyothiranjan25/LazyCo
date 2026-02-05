package com.example.lazyco.entities.UserManagement.RoleModuleResource;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;

public class RoleModuleResourceListener {

  @PrePersist
  public void prePersist(RoleModuleResource roleModuleResource) {
    // Logic to execute before persisting entity
  }

  @PreUpdate
  public void preUpdate(RoleModuleResource roleModuleResource) {
    // Logic to execute before persisting entity
  }

  @PreRemove
  public void preRemove(RoleModuleResource roleModuleResource) {
    // Logic to execute before persisting entity
  }
}

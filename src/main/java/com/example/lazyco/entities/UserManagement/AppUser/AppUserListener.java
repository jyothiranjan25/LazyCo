package com.example.lazyco.entities.UserManagement.AppUser;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;

public class AppUserListener {

  @PrePersist
  public void prePersist(AppUser appUser) {
    // Logic to execute before persisting entity
  }

  @PreUpdate
  public void preUpdate(AppUser appUser) {
    // Logic to execute before persisting an AppUser entity
  }

  @PreRemove
  public void preRemove(AppUser appUser) {
    // Logic to execute before persisting an AppUser entity
  }
}

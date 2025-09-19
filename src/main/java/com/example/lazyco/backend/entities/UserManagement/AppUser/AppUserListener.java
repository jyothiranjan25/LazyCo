package com.example.lazyco.backend.entities.UserManagement.AppUser;

import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;

public class AppUserListener {

  @PrePersist
  public void prePersist(AppUser appUser) {
    // Logic to execute before persisting an AppUser entity
    ApplicationLogger.info("AppUserListener prePersist");
  }

  @PreUpdate
  public void preUpdate(AppUser appUser) {
    // Logic to execute before persisting an AppUser entity
    ApplicationLogger.info("AppUserListener preUpdate");
  }

  @PreRemove
  public void preRemove(AppUser appUser) {
    // Logic to execute before persisting an AppUser entity
    ApplicationLogger.info("AppUserListener preRemove");
  }
}

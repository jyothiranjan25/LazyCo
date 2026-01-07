package com.example.lazyco.entities.UserManagement.AppUser;

import com.example.lazyco.core.Exceptions.ApplicationException;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;

public class AppUserListener {

  @PrePersist
  public void prePersist(AppUser appUser) {
    // Logic to execute before persisting an AppUser entity
    if (appUser.getUserId() == null) {
      throw new ApplicationException(AppUserMessage.USER_ID_REQUIRED);
    }
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

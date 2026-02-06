package com.example.lazyco.entities.UserManagement.AppUser;

import com.example.lazyco.core.Cache.CacheSingleton;
import com.example.lazyco.core.Utils.CommonConstants;
import jakarta.persistence.*;

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

  @PostPersist
  @PostUpdate
  @PostRemove
  public void deleteCache(AppUser appUser) {
    CacheSingleton.getAppUserCache()
        .remove(CommonConstants.LOGGED_USER.concat(":" + appUser.getId()));
  }
}

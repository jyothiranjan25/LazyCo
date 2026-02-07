package com.example.lazyco.entities.UserManagement.UserRole;

import com.example.lazyco.core.Cache.CacheSingleton;
import com.example.lazyco.core.Utils.CommonConstants;
import jakarta.persistence.*;

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

  @PostPersist
  @PostUpdate
  @PostRemove
  public void deleteCache(UserRole userRole) {
    CacheSingleton.getUserRoleCache()
        .remove(CommonConstants.LOGGED_USER_ROLE.concat(":" + userRole.getId()));
  }
}

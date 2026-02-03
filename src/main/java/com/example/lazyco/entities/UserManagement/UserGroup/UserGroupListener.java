package com.example.lazyco.entities.UserManagement.UserGroup;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;

public class UserGroupListener {

  @PrePersist
  public void prePersist(UserGroup userGroup) {
    // Logic to execute before persisting entity
  }

  @PreUpdate
  public void preUpdate(UserGroup userGroup) {
    // Logic to execute before updating entity
  }

  @PreRemove
  public void preRemove(UserGroup userGroup) {
    // Logic to execute before removing entity
  }
}

package com.example.lazyco.entities.UserManagement.RoleModuleResource;

import com.example.lazyco.core.Exceptions.ApplicationException;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;

public class RoleModuleResourceListener {

  @PrePersist
  public void prePersist(RoleModuleResource roleModuleResource) {
    // Logic to execute before persisting entity
    if (roleModuleResource.getRole() == null)
      throw new ApplicationException(RoleModuleResourceMessage.ROLE_REQUIRED);

    if (roleModuleResource.getModule() == null)
      throw new ApplicationException(RoleModuleResourceMessage.MODULE_REQUIRED);

    if (roleModuleResource.getResource() == null)
      throw new ApplicationException(RoleModuleResourceMessage.RESOURCE_REQUIRED);
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

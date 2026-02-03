package com.example.lazyco.entities.UserManagement.Resource;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;

public class ResourceListener {

  @PrePersist
  public void prePersist(Resource resource) {
    // Logic to execute before persisting entity
    defaultValues(resource);
  }

  @PreUpdate
  public void preUpdate(Resource resource) {
    // Logic to execute before updating entity
    defaultValues(resource);
  }

  @PreRemove
  public void preRemove(Resource resource) {
    // Logic to execute before removing entity
  }

  private void defaultValues(Resource resource) {
    if (resource.getResourceOrder() == null) {
      resource.setResourceOrder(0);
    }

    if (resource.getParentResource() == null) {
      resource.setAction(null);
    }
  }
}

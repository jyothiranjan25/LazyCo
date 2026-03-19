package com.example.lazyco.entities.UserManagement.Module;

import com.example.lazyco.core.Exceptions.ApplicationException;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import org.apache.commons.lang3.StringUtils;

public class ModuleListener {

  @PrePersist
  public void prePersist(Module module) {
    // Logic to execute before persisting entity
  }

  @PreUpdate
  public void preUpdate(Module module) {
    // Logic to execute before persisting entity
  }

  @PreRemove
  public void preRemove(Module module) {
    // Logic to execute before persisting entity
  }
}

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
    validationEntity(module);
  }

  @PreUpdate
  public void preUpdate(Module module) {
    // Logic to execute before persisting entity
    validationEntity(module);
  }

  @PreRemove
  public void preRemove(Module module) {
    // Logic to execute before persisting entity
  }

  private void validationEntity(Module module) {
    if (StringUtils.isEmpty(module.getAction()) && module.getResources() == null) {
      throw new ApplicationException(ModuleMessage.MODULE_ACTION_REQUIRED);
    }
    if (!StringUtils.isEmpty(module.getAction()) && module.getResources() != null) {
      throw new ApplicationException(ModuleMessage.SELECT_ACTION_OR_RESOURCE);
    }
  }
}

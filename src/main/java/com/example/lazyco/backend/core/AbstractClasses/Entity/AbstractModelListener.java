package com.example.lazyco.backend.core.AbstractClasses.Entity;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.util.Date;

public class AbstractModelListener {

  public static final String DEFAULT_USER_GROUP = "DEFAULT";

  @PrePersist
  public void prePersist(AbstractModel source) {
    if (source instanceof AbstractRBACModel modelBase) {
      if (modelBase.getUserGroup() == null) {
        modelBase.setUserGroup(DEFAULT_USER_GROUP);
      }
    }
    source.setCreatedBy("DEFAULT");
    source.setCreatedAt(new Date());
  }

  @PreUpdate
  public void preUpdate(AbstractModel source) {
    source.setUpdatedBy("DEFAULT");
    source.setUpdatedAt(new Date());
  }
}

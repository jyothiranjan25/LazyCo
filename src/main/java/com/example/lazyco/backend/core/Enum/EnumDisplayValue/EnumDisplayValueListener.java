package com.example.lazyco.backend.core.Enum.EnumDisplayValue;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

public class EnumDisplayValueListener {

  @PrePersist
  public void prePersist(EnumDisplayValue entity) {
    // Logic to execute before persisting an EnumDisplayValue entity
  }

  @PreUpdate
  public void preUpdate(EnumDisplayValue entity) {
    // Logic to execute before updating an EnumDisplayValue entity
  }
}

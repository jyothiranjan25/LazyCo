package com.example.lazyco.backend.core.ConfigurationMaster;

import static com.example.lazyco.backend.core.WebMVC.BeanProvider.getBean;

import com.example.lazyco.backend.core.AbstractAction;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;

public class ConfigurationMasterListener {

  @PrePersist
  public void PrePersist(ConfigurationMaster configurationMaster) {
    getBean(AbstractAction.class).initializeSystemProps();
  }

  @PreUpdate
  public void PreUpdate(ConfigurationMaster configurationMaster) {
    getBean(AbstractAction.class).initializeSystemProps();
  }

  @PreRemove
  public void SuperRemoveListener(ConfigurationMaster configurationMaster) {
    getBean(AbstractAction.class).initializeSystemProps();
  }
}

package com.example.lazyco.backend.core.ConfigurationMaster;

import static com.example.lazyco.backend.core.WebMVC.BeanProvider.getBean;

import com.example.lazyco.backend.core.AbstractAction;
import jakarta.persistence.*;
import org.springframework.stereotype.Component;

@Component
public class ConfigurationMasterListener {

  @PostPersist
  public void PostPersist(ConfigurationMaster configurationMaster) {
    getBean(AbstractAction.class).initializeSystemProps();
  }

  @PostUpdate
  public void PreUpdate(ConfigurationMaster configurationMaster) {
    getBean(AbstractAction.class).initializeSystemProps();
  }

  @PostRemove
  public void PostRemove(ConfigurationMaster configurationMaster) {
    getBean(AbstractAction.class).initializeSystemProps();
  }
}

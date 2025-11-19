package com.example.lazyco.backend.core.ConfigurationMaster;

import static com.example.lazyco.backend.core.WebMVC.BeanProvider.getPublisher;

import jakarta.persistence.*;

public class ConfigurationMasterListener {

  @PostRemove
  @PostUpdate
  @PostPersist
  public void PostPersist(ConfigurationMaster configurationMaster) {
    // publish event to notify configuration change
    getPublisher().publishEvent(configurationMaster);
  }
}

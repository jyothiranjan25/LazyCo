package com.example.lazyco.backend.core.ConfigurationMaster;

import com.example.lazyco.backend.core.AbstractClasses.Entity.AbstractRBACModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Getter
@Setter
@Entity
@Table(
    name = "configuration_master",
    indexes = {@Index(name = "idx_configuration_master_config_key", columnList = "config_key")})
@EntityListeners(ConfigurationMasterListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ConfigurationMaster extends AbstractRBACModel {
  @Column(name = "config_key", comment = "Store configuration key")
  private String configKey;

  @Column(name = "config_value", comment = "Store configuration value")
  private String configValue;

  @Column(name = "description", comment = "Store configuration description")
  private String description;

  @Column(
      name = "is_sensitive",
      comment = "Indicates if the configuration is sensitive",
      columnDefinition = "BOOLEAN DEFAULT FALSE")
  private Boolean sensitive;
}

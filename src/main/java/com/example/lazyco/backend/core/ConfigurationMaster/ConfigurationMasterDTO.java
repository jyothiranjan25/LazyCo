package com.example.lazyco.backend.core.ConfigurationMaster;

import com.example.lazyco.backend.core.AbstractClasses.CriteriaBuilder.FieldFiltering.InternalFilterableField;
import com.example.lazyco.backend.core.AbstractClasses.CriteriaBuilder.FilteredEntity;
import com.example.lazyco.backend.core.AbstractClasses.DTO.AbstractDTO;
import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FilteredEntity(type = ConfigurationMaster.class)
public class ConfigurationMasterDTO extends AbstractDTO<ConfigurationMasterDTO> {

  @InternalFilterableField private String configKey;

  @InternalFilterableField private String configValue;

  @InternalFilterableField private String description;

  @InternalFilterableField private Boolean sensitive;

  @Expose(serialize = false, deserialize = false)
  private String sensitiveConfigValue;

  // Encrypt the sensitiveConfigValue before setting it
  public void setSensitiveConfigValue(String sensitiveConfigValue) {
    this.sensitiveConfigValue = CryptoUtil.encrypt(sensitiveConfigValue);
  }

  // Decrypt the sensitiveConfigValue before returning it
  public String getSensitiveConfigValue() {
    return CryptoUtil.decrypt(sensitiveConfigValue);
  }
}

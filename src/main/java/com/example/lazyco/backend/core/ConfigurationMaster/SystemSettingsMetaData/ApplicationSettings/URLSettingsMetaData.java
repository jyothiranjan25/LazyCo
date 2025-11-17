package com.example.lazyco.backend.core.ConfigurationMaster.SystemSettingsMetaData.ApplicationSettings;

import com.example.lazyco.backend.core.ConfigurationMaster.SystemSettingsMetaData.SystemSettingsKeys;
import com.example.lazyco.backend.core.ConfigurationMaster.SystemSettingsMetaData.SystemSettingsMetaData;
import com.example.lazyco.backend.core.ConfigurationMaster.SystemSettingsMetaData.SystemSettingsMetaDataDTO;
import com.example.lazyco.backend.core.Utils.FieldInputType;

public enum URLSettingsMetaData implements SystemSettingsMetaData {
  APP_URL(
      "APP URL",
      "Base URL of the web application.",
      SystemSettingsKeys.APP_URL.getValue(),
      FieldInputType.TEXT,
      "https://example.com"),

  ENV(
      "APP Environment",
      "Env of the web application.",
      SystemSettingsKeys.APP_URL.getValue(),
      FieldInputType.TEXT,
      "Production",
      new String[] {
        "Production", "Testing",
      }),
  ;
  private final SystemSettingsMetaDataDTO metaData;

  URLSettingsMetaData(
      String name,
      String description,
      String configKey,
      FieldInputType inputType,
      String placeholder) {
    this.metaData =
        new SystemSettingsMetaDataDTO(name, description, configKey, inputType, placeholder, null);
  }

  URLSettingsMetaData(
      String name,
      String description,
      String configKey,
      FieldInputType inputType,
      String placeholder,
      String[] options) {
    this.metaData =
        new SystemSettingsMetaDataDTO(
            name, description, configKey, inputType, placeholder, options);
  }

  @Override
  public SystemSettingsMetaDataDTO getMetaData() {
    return metaData;
  }
}

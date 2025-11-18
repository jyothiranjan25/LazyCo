package com.example.lazyco.backend.core.ConfigurationMaster.SystemSettingsMetaData.ApplicationSettings;

import com.example.lazyco.backend.core.ConfigurationMaster.SystemSettingsMetaData.SystemSettingsKeys;
import com.example.lazyco.backend.core.ConfigurationMaster.SystemSettingsMetaData.SystemSettingsMetaData;
import com.example.lazyco.backend.core.ConfigurationMaster.SystemSettingsMetaData.SystemSettingsMetaDataDTO;
import com.example.lazyco.backend.core.Utils.FieldInputType;

public enum EnvironmentSettingsMetaData implements SystemSettingsMetaData {
  SYSTEM_TIMEZONE(
      "System Timezone",
      "System Timezone",
      SystemSettingsKeys.SYSTEM_TIMEZONE.getValue(),
      FieldInputType.TEXT,
      "UTC"),

  CLIENT_TIMEZONE(
      "Client Timezone",
      "Client Timezone",
      SystemSettingsKeys.CLIENT_TIMEZONE.getValue(),
      FieldInputType.TEXT,
      "UTC"),

  TEST_FROZEN_TIME(
      "Test Frozen Time",
      "Set a frozen time for testing purposes. Format: YYYY-MM-DD HH:MM:SS",
      SystemSettingsKeys.TEST_FROZEN_TIME.getValue(),
      FieldInputType.DATETIME,
      "2024-01-01 00:00:00"),
  ;
  private final SystemSettingsMetaDataDTO metaData;

  EnvironmentSettingsMetaData(
      String name,
      String description,
      String configKey,
      FieldInputType inputType,
      String placeholder) {
    this.metaData =
        new SystemSettingsMetaDataDTO(name, description, configKey, inputType, placeholder, null);
  }

  EnvironmentSettingsMetaData(
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

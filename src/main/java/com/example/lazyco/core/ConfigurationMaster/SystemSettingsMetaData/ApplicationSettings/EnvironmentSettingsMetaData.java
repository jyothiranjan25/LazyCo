package com.example.lazyco.core.ConfigurationMaster.SystemSettingsMetaData.ApplicationSettings;

import com.example.lazyco.core.ConfigurationMaster.SystemSettingsMetaData.SystemSettingsKeys;
import com.example.lazyco.core.ConfigurationMaster.SystemSettingsMetaData.SystemSettingsMetaData;
import com.example.lazyco.core.ConfigurationMaster.SystemSettingsMetaData.SystemSettingsMetaDataDTO;
import com.example.lazyco.core.DateUtils.DateTimeProps;
import com.example.lazyco.core.Messages.YamlConfig.ApplicationText;
import com.example.lazyco.core.Utils.CommonConstants;
import com.example.lazyco.core.Utils.FieldInputType;

public enum EnvironmentSettingsMetaData implements SystemSettingsMetaData {
  APPLICATION_ENVIRONMENT(
      "Application Environment",
      "The current environment in which the application is running.",
      SystemSettingsKeys.APPLICATION_ENVIRONMENT.getValue(),
      FieldInputType.SELECT,
      "development",
      new String[] {
        CommonConstants.TEST_MODE, CommonConstants.DEV_MODE, CommonConstants.PROD_MODE
      }),

  APPLICATION_LANGUAGE(
      "Application Language",
      "The default language for the application interface.",
      SystemSettingsKeys.APPLICATION_LANGUAGE.getValue(),
      FieldInputType.SELECT,
      "EN",
      ApplicationText.Language.get().toArray(new String[0])),

  SYSTEM_TIMEZONE(
      "System Timezone",
      "System Timezone",
      SystemSettingsKeys.SYSTEM_TIMEZONE.getValue(),
      FieldInputType.SELECT,
      "UTC",
      DateTimeProps.ZONE_SHORT_IDS.keySet().toArray(new String[0])),

  CLIENT_TIMEZONE(
      "Client Timezone",
      "Client Timezone",
      SystemSettingsKeys.CLIENT_TIMEZONE.getValue(),
      FieldInputType.SELECT,
      "UTC",
      DateTimeProps.ZONE_SHORT_IDS.keySet().toArray(new String[0])),

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

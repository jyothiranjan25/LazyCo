package com.example.lazyco.core.ConfigurationMaster.SystemSettingsMetaData.ApplicationSettings;

import com.example.lazyco.core.ConfigurationMaster.SystemSettingsMetaData.SystemSettingsKeys;
import com.example.lazyco.core.ConfigurationMaster.SystemSettingsMetaData.SystemSettingsMetaData;
import com.example.lazyco.core.ConfigurationMaster.SystemSettingsMetaData.SystemSettingsMetaDataDTO;
import com.example.lazyco.core.Utils.FieldInputType;

public enum RedisCacheSettingsMetaData implements SystemSettingsMetaData {
  REDIS_CACHE_URL(
      "Redis Cache URL",
      "Enter the connection string for the local server of the database you want to connect to.",
      SystemSettingsKeys.REDIS_CACHE_URL.getValue(),
      FieldInputType.TEXT,
      "localhost"),

  REDIS_CACHE_PORT(
      "Redis Cache Port",
      "An Redis Cache port is a communication endpoint that is designed to direct data through a network, from one server to another, to its recipient.",
      SystemSettingsKeys.REDIS_CACHE_PORT.getValue(),
      FieldInputType.NUMBER,
      "6379"),

  REDIS_CACHE_EXPIRATION(
      "Redis Cache Expiration",
      "Enter the expiration time for the cache in minutes.",
      SystemSettingsKeys.REDIS_CACHE_EXPIRATION.getValue(),
      FieldInputType.NUMBER,
      "60"),

  REDIS_CACHE_ENABLE(
      "Enable Redis Cache",
      "Enable Redis Cache for faster data retrieval.",
      SystemSettingsKeys.REDIS_CACHE_ENABLE.getValue(),
      FieldInputType.SELECT,
      "",
      new String[] {"ON", "OFF"});
  private final SystemSettingsMetaDataDTO metaData;

  RedisCacheSettingsMetaData(
      String name,
      String description,
      String configKey,
      FieldInputType inputType,
      String placeholder) {
    this.metaData =
        new SystemSettingsMetaDataDTO(name, description, configKey, inputType, placeholder, null);
  }

  RedisCacheSettingsMetaData(
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
